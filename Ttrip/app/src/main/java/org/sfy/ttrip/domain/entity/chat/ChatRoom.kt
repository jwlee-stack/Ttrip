package org.sfy.ttrip.domain.entity.chat

import java.time.LocalDateTime

data class ChatRoom(
    val imagePath: String?,
    val otherNickname: String,
    val updatedAt: LocalDateTime,
    val lastMessage: String,
    val chatId: Int,
    val otherUuid: String
)