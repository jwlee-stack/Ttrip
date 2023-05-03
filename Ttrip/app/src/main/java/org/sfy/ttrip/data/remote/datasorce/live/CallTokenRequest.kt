package org.sfy.ttrip.data.remote.datasorce.live

import com.google.gson.annotations.SerializedName

data class CallTokenRequest(
    @SerializedName("memberUuid")
    val memberUuid: String,
    @SerializedName("sessionId")
    val sessionId: String
)
