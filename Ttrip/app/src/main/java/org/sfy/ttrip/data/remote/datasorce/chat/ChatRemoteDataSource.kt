package org.sfy.ttrip.data.remote.datasorce.chat

interface ChatRemoteDataSource {

    suspend fun getChatRooms(): List<ChatRoomResponse>

    suspend fun exitChatRoom(body: ExitChatRequest): Boolean

    suspend fun getChatDetail(chatId: Int): List<ChatDetailResponse>

    suspend fun createChatRoom(body: CreateChatRequest): CreateChatResponse

    suspend fun chatMatch(body: ChatMatchRequest): Boolean
}