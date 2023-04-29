package org.sfy.ttrip.domain.entity.auth

data class Token(
    val accessToken: String,
    val refreshToken: String
)