package org.sfy.ttrip.domain.entity.auth

import java.time.LocalDate

data class Auth (
    val phoneNumber: String,
    val nickname: String?,
    val intro: String?,
    val imagePath: String?,
    val fcmToken: String?,
    val gender: String?,
    val birthday: LocalDate?,
    val shareLocation: Boolean,
    val token: Token
)