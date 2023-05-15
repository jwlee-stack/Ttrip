package org.sfy.ttrip.data.remote.datasorce.auth

import com.google.gson.annotations.SerializedName
import org.sfy.ttrip.data.remote.datasorce.base.DataToDomainMapper
import org.sfy.ttrip.domain.entity.auth.AccessToken

data class AccessTokenResponse(
    @SerializedName("grantType")
    val grantType: String?,
    @SerializedName("accessToken")
    val accessToken: String?,
    @SerializedName("refreshToken")
    val refreshToken: String?,
    @SerializedName("accessTokenExpiresIn")
    val accessTokenExpiresIn: String?,
    @SerializedName("nickname")
    val nickname: String?
) : DataToDomainMapper<AccessToken> {
    override fun toDomainModel(): AccessToken =
        AccessToken(
            grantType, accessToken, refreshToken,accessTokenExpiresIn, nickname
        )
}
