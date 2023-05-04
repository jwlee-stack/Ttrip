package org.sfy.ttrip.domain.repository.chat

import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.domain.entity.chat.ChatRoom

interface ChatRepository {

    suspend fun getChatRooms(): Resource<List<ChatRoom>>

    suspend fun exitChatRoom(chatId: Int): Boolean
}