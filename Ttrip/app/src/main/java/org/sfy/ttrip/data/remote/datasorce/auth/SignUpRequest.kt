package org.sfy.ttrip.data.remote.datasorce.auth

import com.google.gson.annotations.SerializedName

data class SignUpRequest(
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("password")
    val password: String
)
