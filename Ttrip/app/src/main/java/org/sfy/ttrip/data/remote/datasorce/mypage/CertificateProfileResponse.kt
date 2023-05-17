package org.sfy.ttrip.data.remote.datasorce.mypage

import com.google.gson.annotations.SerializedName

data class CertificateProfileResponse(
    @SerializedName("type")
    val type: Int,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("extraData")
    val extraData: String,
    @SerializedName("result")
    val result: Boolean
)
