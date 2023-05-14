package org.sfy.ttrip.data.remote.datasorce.auth

import com.google.gson.annotations.SerializedName

data class AccessTokenRequest(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String
)
