package org.sfy.ttrip.presentation.live

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.VisibleRegion
import dagger.hilt.android.AndroidEntryPoint
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.FragmentLiveBinding
import org.sfy.ttrip.presentation.base.BaseFragment

@AndroidEntryPoint
class LiveFragment : BaseFragment<FragmentLiveBinding>(R.layout.fragment_live), OnMapReadyCallback,
    GoogleMap.OnCameraMoveListener {

    private lateinit var map: GoogleMap
    private lateinit var visibleRegion: VisibleRegion
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // 권한이 부여된 경우 현재 위치 가져오기
            getDeviceLocation()
        } else {
            // 권한이 거부된 경우 처리
            showToast("위치 권한이 필요합니다.")
        }
    }

    override fun initView() {
        setMapView()
        initListener()
        requestLocationPermission()
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
        if (::map.isInitialized) {
            visibleRegion = map.projection.visibleRegion
            val latLngBounds: LatLngBounds = visibleRegion.latLngBounds
            val northeast: LatLng = latLngBounds.northeast
            val southwest: LatLng = latLngBounds.southwest
            // API 호출에 필요한 작업 수행
        }
    }

    private fun initListener() {
        binding.apply {
            ivCurrentLocation.setOnClickListener {
                getDeviceLocation()
            }
            switchLive.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    tvSwitchState.apply {
                        setText(R.string.content_live_toggle_on)
                        setTextColor(ContextCompat.getColor(requireContext(), R.color.neon_blue))
                        showToast("LIVE 모드가 시작됩니다.")
                    }
                } else {
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
    private fun getDeviceLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        try {
            // 위치 요청 설정
            val locationRequest = LocationRequest.create().apply {
                interval = 10000
                fastestInterval = 5000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            // 위치 요청 시작
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        // 위치 결과를 가져옴
                        val location = locationResult.lastLocation
                        if (location != null) {
                            // 위치가 null이 아니면 지도 이동
                            moveCamera(LatLng(location.latitude, location.longitude))
                            // 위치 정보 수신 중지
                            fusedLocationClient.removeLocationUpdates(this)
                        }
                    }
                },
                null
            )
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message ?: "")
        }
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
            getDeviceLocation()
        }
    }

    // 전화 요청시 벨소리
    private fun setRingtone() {
        val mediaPlayer = MediaPlayer.create(context, R.raw.ringtone)
        mediaPlayer.start()
    }
}