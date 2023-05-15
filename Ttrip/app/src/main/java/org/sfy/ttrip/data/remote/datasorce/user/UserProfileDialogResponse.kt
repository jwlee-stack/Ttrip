package org.sfy.ttrip.data.remote.datasorce.user

import com.google.gson.annotations.SerializedName
import org.sfy.ttrip.data.remote.datasorce.base.DataToDomainMapper
import org.sfy.ttrip.domain.entity.user.UserProfileDialog

data class UserProfileDialogResponse(
    @SerializedName("uuid")
    val uuid: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("intro")
    val intro: String,
    @SerializedName("profileImgPath")
    val profileImgPath: String,
    @SerializedName("markerImgPath")
    val markerImgPath: String,
    @SerializedName("backgroundImgPath")
    val backgroundImgPath: String?,
    @SerializedName("fcmToken")
    val fcmToken: String?,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("age")
    val age: Int,
    @SerializedName("shareLocation")
    val shareLocation: Boolean,
    @SerializedName("profileVerification")
    val profileVerification: Boolean,
) : DataToDomainMapper<UserProfileDialog> {
    override fun toDomainModel(): UserProfileDialog =
        UserProfileDialog(
            uuid,
            phoneNumber,
            nickname,
            intro,
            profileImgPath,
            markerImgPath,
            backgroundImgPath,
            fcmToken,
            gender,
            age,
            shareLocation,
            profileVerification
        )
}