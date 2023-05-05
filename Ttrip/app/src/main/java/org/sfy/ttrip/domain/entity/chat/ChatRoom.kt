package org.sfy.ttrip.domain.entity.chat

data class ChatRoom(
    val imagePath: String?,
    val otherNickname: String,
    val updatedAt: String,
    val lastMessage: String,
    val chatId: Int,
    val otherUuid: String,
    val articleTitle: String
)