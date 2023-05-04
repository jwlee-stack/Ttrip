package org.sfy.ttrip.data.remote.datasorce.chat

interface ChatRemoteDataSource {

    suspend fun getChatRooms(): List<ChatRoomResponse>

    suspend fun exitChatRoom(body: ExitChatRequest): Boolean
}