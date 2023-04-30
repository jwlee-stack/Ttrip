package org.sfy.ttrip.domain.entity.live

data class LiveUser(
    val nickname: String?,
    val gender: String,
    val age: String,
    val memberUuid: String,
    val latitude: Double,
    val longitude: Double,
    val matchingRate: Double,
    val distanceFromMe: Double
)