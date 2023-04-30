package org.sfy.ttrip.domain.entity.auth

import java.time.LocalDate

data class Auth (
    val uuid: String,
    val phoneNumber: String,
    val nickname: String?,
    val intro: String?,
    val profileImgPath: String?,
    val markerImgPath: String?,
    val fcmToken: String?,
    val gender: String?,
    val birthday: LocalDate?,
    val shareLocation: Boolean,
    val token: Token
)