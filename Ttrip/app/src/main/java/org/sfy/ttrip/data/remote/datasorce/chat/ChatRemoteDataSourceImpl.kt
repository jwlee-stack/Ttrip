package org.sfy.ttrip.data.remote.datasorce.chat

import org.sfy.ttrip.data.remote.service.ChatApiService
import javax.inject.Inject

class ChatRemoteDataSourceImpl @Inject constructor(
    private val chatApiService: ChatApiService
) : ChatRemoteDataSource {

    override suspend fun getChatRooms(): List<ChatRoomResponse> =
        chatApiService.getChatRooms().data!!

    override suspend fun exitChatRoom(body: ExitChatRequest): Boolean =
        chatApiService.exitChatRoom(body).data!!

    override suspend fun getChatDetail(chatId: Int): List<ChatDetailResponse> =
        chatApiService.getChatDetail(chatId).data!!

    override suspend fun createChatRoom(body: CreateChatRequest) =
        chatApiService.createChatRoom(body)

    override suspend fun chatMatch(body: ChatMatchRequest): Boolean =
        chatApiService.chatMatch(body).data!!
}