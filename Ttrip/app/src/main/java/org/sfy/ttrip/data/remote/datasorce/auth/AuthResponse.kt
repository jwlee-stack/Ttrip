package org.sfy.ttrip.data.remote.datasorce.auth

import com.google.gson.annotations.SerializedName
import org.sfy.ttrip.data.remote.datasorce.base.DataToDomainMapper
import org.sfy.ttrip.domain.entity.user.Auth
import java.time.LocalDate

data class AuthResponse(
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("nickname")
    val nickname: String?,
    @SerializedName("intro")
    val intro: String?,
    @SerializedName("imagePath")
    val imagePath: String?,
    @SerializedName("fcmToken")
    val fcmToken: String?,
    @SerializedName("gender")
    val gender: String?,
    @SerializedName("birthday")
    val birthday: LocalDate?,
    @SerializedName("shareLocation")
    val shareLocation: Boolean,
    @SerializedName("tokenDto")
    val tokenResponse: TokenResponse
) : DataToDomainMapper<Auth> {
    override fun toDomainModel(): Auth =
        Auth(
            phoneNumber,
            nickname,
            intro,
            imagePath,
            fcmToken,
            gender,
            birthday,
            shareLocation,
            tokenResponse.toDomainModel()
        )
}