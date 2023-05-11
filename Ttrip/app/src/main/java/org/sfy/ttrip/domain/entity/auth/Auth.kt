package org.sfy.ttrip.domain.entity.auth

data class Auth (
    val uuid: String,
    val phoneNumber: String,
    val nickname: String?,
    val intro: String?,
    val profileImgPath: String?,
    val markerImgPath: String?,
    val fcmToken: String?,
    val gender: String?,
    val age: Int?,
    val shareLocation: Boolean,
    val token: Token
)