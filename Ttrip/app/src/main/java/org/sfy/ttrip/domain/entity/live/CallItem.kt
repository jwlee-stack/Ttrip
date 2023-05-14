package org.sfy.ttrip.domain.entity.live

data class CallItem (
    val type: String,
    val sessionId: String,
    val memberUuid: String,
    val otherUuid: String
)