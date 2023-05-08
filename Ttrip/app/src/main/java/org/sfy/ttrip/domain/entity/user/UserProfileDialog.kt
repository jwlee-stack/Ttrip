package org.sfy.ttrip.domain.entity.user

data class UserProfileDialog(
    val uuid: String,
    val phoneNumber: String,
    val nickname: String,
    val intro: String,
    val profileImgPath: String,
    val markerImgPath: String,
    val backgroundImgPath: String?,
    val fcmToken: String?,
    val gender: String,
    val age: Int,
    val shareLocation: Boolean,
)
