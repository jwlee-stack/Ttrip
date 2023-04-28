package org.sfy.ttrip.data.remote.datasorce.auth

import com.google.gson.annotations.SerializedName
import org.sfy.ttrip.data.remote.datasorce.base.DataToDomainMapper
import org.sfy.ttrip.domain.entity.user.Token

data class TokenResponse(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String
) : DataToDomainMapper<Token> {
    override fun toDomainModel(): Token =
        Token(
            accessToken, refreshToken
        )
}