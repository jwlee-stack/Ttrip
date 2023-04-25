package org.sfy.ttrip.presentation.live

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.VisibleRegion
import dagger.hilt.android.AndroidEntryPoint
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.FragmentBoardBinding
import org.sfy.ttrip.presentation.base.BaseFragment

@AndroidEntryPoint
class LiveFragment : BaseFragment<FragmentBoardBinding>(R.layout.fragment_live), OnMapReadyCallback,
    GoogleMap.OnCameraMoveListener {

    private lateinit var map: GoogleMap
    private lateinit var visibleRegion: VisibleRegion

    override fun initView() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setOnCameraMoveListener(this)
    }

    override fun onCameraMove() {
        visibleRegion = map.projection.visibleRegion
        val latLngBounds: LatLngBounds = visibleRegion.latLngBounds
        val northeast: LatLng = latLngBounds.northeast
        val southwest: LatLng = latLngBounds.southwest
        // API 호출에 필요한 작업 수행
    }
}