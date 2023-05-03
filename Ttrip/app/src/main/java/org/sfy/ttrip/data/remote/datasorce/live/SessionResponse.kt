package org.sfy.ttrip.data.remote.datasorce.live

import com.google.gson.annotations.SerializedName
import org.sfy.ttrip.data.remote.datasorce.base.DataToDomainMapper
import org.sfy.ttrip.domain.entity.live.SessionItem

data class SessionResponse(
    @SerializedName("sessionId")
    val sessionId: String
) : DataToDomainMapper<SessionItem> {
    override fun toDomainModel(): SessionItem =
        SessionItem(sessionId)
}