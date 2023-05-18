package org.sfy.ttrip.data.remote.datasorce.mypage

import com.google.gson.annotations.SerializedName
import org.sfy.ttrip.data.remote.datasorce.base.DataToDomainMapper
import org.sfy.ttrip.domain.entity.mypage.UserProfile

data class UserProfileResponse(
    @SerializedName("uuid")
    val uuid: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("nickname")
    val nickname: String?,
    @SerializedName("profileImgPath")
    val profileImgPath: String?,
    @SerializedName("markerImgPath")
    val markerImgPath: String?,
    @SerializedName("backgroundImgPath")
    val backgroundImgPath: String?,
    @SerializedName("intro")
    val intro: String?,
    @SerializedName("gender")
    val gender: String?,
    @SerializedName("age")
    val age: String?,
    @SerializedName("fcmToken")
    val fcmToken: String?,
    @SerializedName("shareLocation")
    val shareLocation: Boolean,
    @SerializedName("profileVerification")
    val profileVerification: Boolean
) : DataToDomainMapper<UserProfile> {
    override fun toDomainModel(): UserProfile =
        UserProfile(
            uuid,
            phoneNumber,
            nickname,
            profileImgPath,
            markerImgPath,
            backgroundImgPath,
            intro,
            gender,
            age,
            fcmToken,
            shareLocation,
            profileVerification
        )
}
