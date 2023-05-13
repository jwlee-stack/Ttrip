package org.sfy.ttrip.domain.entity.landmark

data class DoodleItem(
    val doodleId: Int,
    val latitude: Double,
    val positionX: Double,
    val positionY: Double,
    val positionZ: Double,
    val doodleImgPath: String,
    val landmarkId: Int
)
