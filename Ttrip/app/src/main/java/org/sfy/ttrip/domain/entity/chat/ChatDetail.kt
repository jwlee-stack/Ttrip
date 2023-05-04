package org.sfy.ttrip.domain.entity.chat

import java.time.LocalDateTime

data class ChatDetail(
    val isMine: Boolean,
    val content: String,
    val createdDate: LocalDateTime
)
