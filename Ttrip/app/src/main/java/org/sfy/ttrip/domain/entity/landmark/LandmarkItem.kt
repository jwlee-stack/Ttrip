package org.sfy.ttrip.domain.entity.landmark

data class LandmarkItem(
    val landmarkId: Int,
    val landmarkName: String,
    val latitude: Double,
    val longitude: Double
)