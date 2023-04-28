package org.sfy.ttrip.domain.entity.user

import com.google.gson.annotations.SerializedName
import org.sfy.ttrip.data.remote.datasorce.auth.TokenResponse
import java.time.LocalDate

data class Auth (
    val phoneNumber: String,
    val nickname: String,
    val intro: String,
    val imagePath: String,
    val fcmToken: String,
    val gender: String,
    val birthday: LocalDate,
    val token: Token
)