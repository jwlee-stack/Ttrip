package org.sfy.ttrip.data.remote.datasorce.live

import com.google.gson.annotations.SerializedName
import org.sfy.ttrip.data.remote.datasorce.base.DataToDomainMapper
import org.sfy.ttrip.domain.entity.live.CallToken

data class CallTokenResponse(
    @SerializedName("openViduToken")
    val openViduToken: String
) : DataToDomainMapper<CallToken> {
    override fun toDomainModel(): CallToken =
        CallToken(openViduToken)
}