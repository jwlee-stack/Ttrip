package org.sfy.ttrip.presentation.live

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.location.Geocoder
import android.media.MediaPlayer
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.sfy.ttrip.ApplicationClass
import org.sfy.ttrip.MainActivity
import org.sfy.ttrip.R
import org.sfy.ttrip.common.util.UserProfileDialog
import org.sfy.ttrip.common.util.UserProfileDialogListener
import org.sfy.ttrip.databinding.FragmentLiveBinding
import org.sfy.ttrip.presentation.base.BaseFragment
import org.sfy.ttrip.presentation.chat.ChatViewModel
import org.sfy.ttrip.presentation.landmark.LandmarkViewModel
import java.util.*
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@AndroidEntryPoint
class LiveFragment : BaseFragment<FragmentLiveBinding>(R.layout.fragment_live), OnMapReadyCallback,
    GoogleMap.OnCameraIdleListener, CloseLiveDialogListener, UserProfileDialogListener,
    GoogleMap.OnMarkerClickListener, StartARDialogListener {

    private lateinit var callback: OnBackPressedCallback
    private lateinit var map: GoogleMap
    private lateinit var visibleRegion: VisibleRegion
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var landmarkTag: String

    private var waitTime = 0L
    private val liveViewModel by viewModels<LiveViewModel>()
    private val chatViewModel by viewModels<ChatViewModel>()
    private val landmarkViewModel by activityViewModels<LandmarkViewModel>()

    private val liveUserAdapter by lazy {
        LiveUserAdapter(
            this::getLiveUser,
            this::callToOtherUser
        )
    }
    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // 권한이 부여된 경우 현재 위치 가져오기
            setLocationService()
        } else {
            // 권한이 거부된 경우 처리
            showToast("위치 권한이 필요합니다.")
        }
    }

    override fun initView() {
        (activity as MainActivity).hideBottomNavigation(false)
        blockMoveToOtherMenu()
        setLiveUsersRecyclerView()
        observeLiveState()
        setMapView()
        initListener()
        requestLocationPermission()
        getFilteredList()
        getOpenViduToken()
        showUserProfileDialog()
        initObserve()

        if (!ApplicationClass.preferences.live) {
            showToast("상단의 라이브 버튼을 켜서 이용하세요!")
            ApplicationClass.preferences.live = true
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (System.currentTimeMillis() - waitTime >= 2500) {
                    waitTime = System.currentTimeMillis()
                    showToast("뒤로가기 버튼을\n한번 더 누르면 종료됩니다.")
                } else {
                    requireActivity().finishAffinity()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.switchLive.isChecked = false
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setOnMarkerClickListener(this)
        map.setOnCameraIdleListener(this)
        // My Location 레이어 활성화
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        } else {
            map.apply {
                isMyLocationEnabled = true
                uiSettings.isMyLocationButtonEnabled = false
                setMaxZoomPreference(20f)
                setMinZoomPreference(5f)
            }
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        landmarkTag = marker.tag as String
        val value = landmarkTag.split(",")
        if (distance(
                value[1].toDouble(),
                value[2].toDouble(),
                liveViewModel.lat,
                liveViewModel.lng
            ) <= 200.0
        ) {
            landmarkViewModel.issueBadge(landmarkTag.split(",")[0].toInt())
            val dialog = StartARDialog(requireContext(), this)
            dialog.show()
        } else {
            showToast("랜드마크와의 거리가 멀어\n접근할 수 없습니다.")
        }
        return true
    }

    override fun onCameraIdle() {
        getFilteredList()
    }

    override fun onConfirmButtonClicked() {
        binding.switchLive.isChecked = false
    }

    override fun postChats(boardId: Int, uuid: String) {
        chatViewModel.createChatRoom(boardId, uuid)
    }

    override fun clear() {
        liveViewModel.clearUserProfile()
    }

    private fun setLandmarks() {
        liveViewModel.landmarks.observe(viewLifecycleOwner) {
            if (::map.isInitialized) {
                it?.let {
                    for (item in it) {
                        try {
                            val vectorDrawable = ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.ic_landmark_marker
                            )
                            val bitmap = Bitmap.createBitmap(
                                vectorDrawable?.intrinsicWidth ?: 0,
                                vectorDrawable?.intrinsicHeight ?: 0,
                                Bitmap.Config.ARGB_8888
                            )
                            val canvas = Canvas(bitmap)
                            vectorDrawable?.setBounds(0, 0, canvas.width, canvas.height)
                            vectorDrawable?.draw(canvas)

                            // 마커 이미지의 크기를 조정합니다.
                            val scaleFactor = 0.5f
                            val scaledBitmap = Bitmap.createScaledBitmap(
                                bitmap,
                                (bitmap.width * scaleFactor).toInt(),
                                (bitmap.height * scaleFactor).toInt(),
                                false
                            )

                            val markerBitmapDescriptor =
                                BitmapDescriptorFactory.fromBitmap(scaledBitmap)
                            val markerOptions = MarkerOptions()
                                .position(LatLng(item.latitude, item.longitude))
                                .icon(markerBitmapDescriptor)

                            val marker = map.addMarker(markerOptions)
                            marker?.tag =
                                "${item.landmarkId},${item.latitude},${item.longitude},${item.landmarkName}"

                        } catch (e: Exception) {
                            Log.e("setLandmarks", "Failed to decode marker image: ${e.message}")
                        }
                    }
                }
            }
        }
        liveViewModel.getLandmarks()
    }

    private fun showUserProfileDialog() {
        liveViewModel.userProfile.observe(viewLifecycleOwner) {
            it?.let {
                UserProfileDialog(
                    requireActivity(),
                    this,
                    it.nickname,
                    18,
                    it.uuid,
                    it.backgroundImgPath,
                    it.profileImgPath,
                    liveViewModel.matchingRate,
                    it.age,
                    it.gender,
                    it.intro,
                    it.profileVerification
                ).show()
            }
        }
    }

    private fun blockMoveToOtherMenu() {
        val bottomNavigationView =
            (activity as? MainActivity)?.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView?.setOnNavigationItemSelectedListener { menuItem ->
            if (binding.switchLive.isChecked) {
                val dialog = CloseLiveDialog(requireContext(), this)
                dialog.show()
                false
            } else {
                Navigation.findNavController(requireActivity(), R.id.nav_host)
                    .navigate(menuItem.itemId)
                true
            }
        }
    }

    private fun observeLiveState() {
        liveViewModel.liveOn.observe(viewLifecycleOwner) { liveOn ->
            liveViewModel.cityOnLive.observe(viewLifecycleOwner) { city ->
                if (liveOn == true && city != "") {
                    liveViewModel.apply {
                        if (city != null) {
                            this.getLiveUsers(city, lng, lat)
                            getFilteredList()
                        }
                    }
                } else {
                    liveViewModel.setLiveUserReset()
                }
            }
        }
        landmarkViewModel.issueStatus.observe(viewLifecycleOwner) {
            if (it == 200) {
                showToast("뱃지가 발급되었습니다!")
                landmarkViewModel.clearIssueStatus()
            } else if (it == 204) {
                showToast("이미 발급된 뱃지입니다.")
                landmarkViewModel.clearIssueStatus()
            }
        }
    }

    private fun setLiveUsersRecyclerView() {
        binding.rvLiveUsers.apply {
            adapter = liveUserAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        }
        liveViewModel.filteredLiveUserList.observe(viewLifecycleOwner) { response ->
            response?.let {
                map.clear()
                liveUserAdapter.setLiveUser(it.map { users -> users!! })
                it.forEach { liveUser ->
                    liveUser?.let {
                        val latLng = LatLng(liveUser.latitude, liveUser.longitude)
                        Glide.with(requireContext())
                            .asBitmap()
                            .load("http://k8d104.p.ssafy.io:8081/images${liveUser.markerImgPath!!}")
                            .into(object : CustomTarget<Bitmap>() {
                                override fun onResourceReady(
                                    resource: Bitmap,
                                    transition: Transition<in Bitmap>?
                                ) {
                                    val resizedBitmap =
                                        Bitmap.createScaledBitmap(resource, 150, 170, false)
                                    map.addMarker(
                                        MarkerOptions()
                                            .position(latLng)
                                            .icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap))
                                    )
                                }

                                override fun onLoadCleared(placeholder: Drawable?) {}
                            })
                    }
                }
                setLandmarks()
            }
        }
    }

    // 범위 안에 있는 유저들만 표시
    private fun getFilteredList() {
        if (::map.isInitialized) {
            visibleRegion = map.projection.visibleRegion
            val latLngBounds: LatLngBounds = visibleRegion.latLngBounds
            liveViewModel.liveUserList.observe(this@LiveFragment) {
                liveViewModel.filteredLiveUserList.value =
                    it?.filter { user ->
                        val userLatLng = LatLng(user!!.latitude, user.longitude)
                        latLngBounds.contains(userLatLng)
                    }
            }
        }
    }

    private fun initListener() {
        binding.apply {
            ivCurrentLocation.setOnClickListener {
                if (switchLive.isChecked) {
                    startLocationUpdates()
                }
            }

            switchLive.setOnCheckedChangeListener { _, isChecked ->
                if ((activity as MainActivity).checkLocationService()) {
                    if (isChecked) {
                        tvSwitchState.apply {
                            setText(R.string.content_live_toggle_on)
                            setTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.neon_blue
                                )
                            )
                            showToast("LIVE 모드가 시작됩니다.")
                            liveViewModel.liveOn.value = true
                            startLocationUpdates()
                        }
                    } else {
                        tvSwitchState.apply {
                            setText(R.string.content_live_toggle_off)
                            setTextColor(ContextCompat.getColor(requireContext(), R.color.grey))
                            liveViewModel.apply {
                                liveOn.value = false
                                cityOnLive.value = ""
                                lat = 0.0
                                lng = 0.0
                                disconnectSocket()
                            }
                            stopLocationUpdates()
                            liveViewModel.apply {
                                setLiveUserReset()
                                filteredLiveUserList.value = null
                            }
                            liveUserAdapter.setLiveUser(null)
                            map.clear()
                        }
                    }
                } else {
                    showToast("GPS를 먼저 켜주세요")
                    switchLive.isChecked = false
                    tvSwitchState.apply {
                        setText(R.string.content_live_toggle_off)
                        setTextColor(ContextCompat.getColor(requireContext(), R.color.grey))
                    }
                }
            }
        }
    }

    private fun setMapView() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    // 현재 위치 가져오기
    private fun setLocationService() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                // 위치 결과를 가져옴
                val location = locationResult.lastLocation
                if (location != null) {
                    // 뷰모델 위도, 경도 초기 업데이트
                    liveViewModel.apply {
                        if (this.lat == 0.0 && this.lng == 0.0) {
                            this.lat = location.latitude
                            this.lng = location.longitude
                        }
                    }
                    // 100m 이상 이동할 경우 업데이트
                    updateLocation(location.latitude, location.longitude)
                    // 위치가 null이 아니면 지도 이동
                    moveCamera(LatLng(location.latitude, location.longitude))
                    // 현재 도시의 이름을 받아오기
                    val geocoder = Geocoder(requireContext(), Locale.ENGLISH)
                    val addresses =
                        geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    val cityName = addresses!![0].locality?.trim()?.replace("-", "")

                    if (liveViewModel.cityOnLive.value == "") {
                        liveViewModel.cityOnLive.value = cityName
                        // 소켓 연결
                        liveViewModel.connectSocket(
                            liveViewModel.cityOnLive.value.toString(),
                            ApplicationClass.preferences.userId.toString()
                        )
                        liveViewModel.sendMyInfo(
                            liveViewModel.cityOnLive.value.toString(),
                            ApplicationClass.preferences.userId.toString(),
                            location.latitude,
                            location.longitude,
                            ApplicationClass.preferences.nickname.toString(),
                            ApplicationClass.preferences.gender.toString(),
                            ApplicationClass.preferences.age.toString(),
                            ApplicationClass.preferences.profileImgPath.toString(),
                            ApplicationClass.preferences.markerImgPath.toString()
                        )
                    } else if (liveViewModel.cityOnLive.value != cityName) {
                        binding.switchLive.isChecked = false
                    }
                    // 위치 정보 수신 중지
                    fusedLocationClient.removeLocationUpdates(this)
                }
            }
        }
    }

    // 위치 정보 수신
    private fun startLocationUpdates() {
        try {
            val locationRequest = LocationRequest.create().apply {
                interval = 10000
                fastestInterval = 5000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            // 위치 요청 시작
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message ?: "")
        }
    }

    // 위치 정보 수신 중지
    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    // 지도 이동
    private fun moveCamera(latLng: LatLng) {
        if (::map.isInitialized) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        }
    }

    // 위치 권한 확인
    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 권한이 부여되어 있지 않은 경우 권한 요청
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            // 권한이 이미 부여되어 있는 경우 처리할 로직
            setLocationService()
        }
    }

    // 전화 요청시 벨소리
    private fun setRingtone() {
        val mediaPlayer = MediaPlayer.create(context, R.raw.ringtone)
        mediaPlayer.start()
    }

    fun updateLocation(latitude: Double, longitude: Double) {
        // 마지막 업데이트 시간 계산
        val currentTime = System.currentTimeMillis()
        val lastUpdateTime = liveViewModel.lastUpdateTime
        val timeDiff = currentTime - lastUpdateTime

        // 100m 이상 이동한 경우에만 업데이트
        if (timeDiff >= 1000 * 60 * 1 // 1분 이상 지난 경우
            || distance(
                latitude,
                longitude,
                liveViewModel.lat,
                liveViewModel.lng
            ) <= 100.0
        ) {
            liveViewModel.apply {
                this.lat = latitude
                this.lng = longitude
                this.lastUpdateTime = currentTime
            }
            showToast("위치가 업데이트 되었습니다.")
        }
    }

    private fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val standard = 6371 // km
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val lat1 = Math.toRadians(lat1)
        val lat2 = Math.toRadians(lat2)

        val a = sin(dLat / 2) * sin(dLat / 2) +
                sin(dLon / 2) * sin(dLon / 2) * cos(lat1) * cos(lat2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        val d = standard * c
        return d * 1000 // m
    }

    private fun callToOtherUser(memberId: String) {
        lifecycleScope.launch {
            val async = liveViewModel.createCallingSession()
            liveViewModel.getCallToken(
                async.toString(),
                ApplicationClass.preferences.userId.toString()
            )
        }
    }

    private fun getOpenViduToken() {
        liveViewModel.sessionId.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                val value = liveViewModel.getCallToken(
                    it,
                    ApplicationClass.preferences.userId.toString()
                )
            }
        }
    }

    private fun getLiveUser(nickname: String, matchingRate: Float) {
        liveViewModel.getUserProfile(nickname)
        liveViewModel.matchingRate = matchingRate
    }

    private fun initObserve() {
        chatViewModel.chatInit.observe(this@LiveFragment) {
            if (it != null) {
                navigate(
                    LiveFragmentDirections.actionLiveFragmentToChatDetailFragment(
                        it.chatId,
                        it.memberUuid,
                        it.imagePath,
                        it.articleTitle,
                        it.nickname,
                        it.articleId,
                        it.isMatch,
                        it.status.toString()
                    )
                )

                chatViewModel.clearChatInit()
            }
        }
    }

    override fun onStartARButtonClicked() {
        val value = landmarkTag.split(",")
        navigate(LiveFragmentDirections.actionLiveFragmentToDoodleFragment(value[0].toInt()))
        (activity as MainActivity).hideBottomNavigation(true)
    }
}