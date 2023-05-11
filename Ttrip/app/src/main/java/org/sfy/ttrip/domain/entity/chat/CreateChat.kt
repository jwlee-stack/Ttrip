package org.sfy.ttrip.domain.entity.chat

data class CreateChat(
    val nickname: String,
    val imagePath: String?,
    val memberUuid: String,
    val similarity: Double,
    val lastMessage: String,
    val chatId: Int,
    val updatedAt: String,
    val articleTitle: String,
    val articleId: Int,
    val status: Char,
    val isMatch: Boolean
)