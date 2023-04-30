package org.sfy.ttrip.data.remote.datasorce.live

import com.google.gson.annotations.SerializedName
import org.sfy.ttrip.data.remote.datasorce.base.DataToDomainMapper
import org.sfy.ttrip.domain.entity.live.LiveUser

data class LiveUserResponse(
    @SerializedName("nickname")
    val nickname: String?,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("age")
    val age: String,
    @SerializedName("memberUuid")
    val memberUuid: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("matchingRate")
    val matchingRate: Double,
    @SerializedName("distanceFromMe")
    val distanceFromMe: Double
) : DataToDomainMapper<LiveUser> {
    override fun toDomainModel(): LiveUser =
        LiveUser(
            nickname,
            gender,
            age,
            memberUuid,
            latitude,
            longitude,
            matchingRate,
            distanceFromMe
        )
}