package org.sfy.ttrip.data.remote.datasorce.auth

import com.google.gson.annotations.SerializedName
import org.sfy.ttrip.data.remote.datasorce.base.DataToDomainMapper
import org.sfy.ttrip.domain.entity.auth.Auth

data class AuthResponse(
    @SerializedName("uuid")
    val uuid: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("nickname")
    val nickname: String?,
    @SerializedName("intro")
    val intro: String?,
    @SerializedName("profileImgPath")
    val profileImgPath: String?,
    @SerializedName("markerImgPath")
    val markerImgPath: String?,
    @SerializedName("fcmToken")
    val fcmToken: String?,
    @SerializedName("gender")
    val gender: String?,
    @SerializedName("age")
    val age: Int?,
    @SerializedName("shareLocation")
    val shareLocation: Boolean,
    @SerializedName("isFreeze")
    val isFreeze: Boolean,
    @SerializedName("tokenDto")
    val tokenResponse: TokenResponse
) : DataToDomainMapper<Auth> {
    override fun toDomainModel(): Auth =
        Auth(
            uuid,
            phoneNumber,
            nickname,
            intro,
            profileImgPath,
            markerImgPath,
            fcmToken,
            gender,
            age,
            shareLocation,
            isFreeze,
            tokenResponse.toDomainModel()
        )
}