package org.sfy.ttrip.presentation.live

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.media.MediaPlayer
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.VisibleRegion
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.sfy.ttrip.ApplicationClass
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.FragmentLiveBinding
import org.sfy.ttrip.presentation.base.BaseFragment
import org.sfy.ttrip.presentation.init.AuthViewModel
import java.util.*
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@AndroidEntryPoint
class LiveFragment : BaseFragment<FragmentLiveBinding>(R.layout.fragment_live), OnMapReadyCallback,
    GoogleMap.OnCameraMoveListener {

    private lateinit var map: GoogleMap
    private lateinit var visibleRegion: VisibleRegion
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    private val authViewModel by activityViewModels<AuthViewModel>()
    private val liveViewModel by viewModels<LiveViewModel>()
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
        setLiveUsersRecyclerView()
        observeLiveState()
        setMapView()
        initListener()
        requestLocationPermission()
        getFilteredList()
        getOpenViduToken()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setOnCameraMoveListener(this)
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

    override fun onCameraMove() {
        getFilteredList()
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
    }

    private fun setLiveUsersRecyclerView() {
        binding.rvLiveUsers.apply {
            adapter = liveUserAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        }
        liveViewModel.filteredLiveUserList.observe(viewLifecycleOwner) { response ->
            response?.let { liveUserAdapter.setLiveUser(it.map { users -> users!! }) }
        }
    }

    // 범위 안에 있는 유저들만 표시
    private fun getFilteredList() {
        if (::map.isInitialized) {
            visibleRegion = map.projection.visibleRegion
            val latLngBounds: LatLngBounds = visibleRegion.latLngBounds
            liveViewModel.liveUserList.observe(viewLifecycleOwner) {
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
                if (isChecked) {
                    tvSwitchState.apply {
                        setText(R.string.content_live_toggle_on)
                        setTextColor(ContextCompat.getColor(requireContext(), R.color.neon_blue))
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
                            liveViewModel.lat,
                            liveViewModel.lng,
                            "nickname",
                            "gender",
                            "20대",
                            "profilepath",
                            "markerpath"
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

    private fun getLiveUser(memberUuid: String) {}
}