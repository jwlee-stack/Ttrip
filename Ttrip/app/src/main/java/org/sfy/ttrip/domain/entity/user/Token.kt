package org.sfy.ttrip.domain.entity.user

data class Token(
    val accessToken: String,
    val refreshToken: String
)