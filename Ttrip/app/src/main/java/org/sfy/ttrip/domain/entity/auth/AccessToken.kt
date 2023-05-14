package org.sfy.ttrip.domain.entity.auth

data class AccessToken(
    val grantType: String,
    val accessToken: String,
    val refreshToken: String
)
