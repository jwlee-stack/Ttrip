package org.sfy.ttrip.domain.entity.chat

data class CreateChat(
    val imagePath: String?,
    val nickname: String,
    val updatedAt: String,
    val lastMessage: String,
    val chatId: Int,
    val uuid: String
)