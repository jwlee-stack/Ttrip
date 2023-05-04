package org.sfy.ttrip.data.remote.datasorce.chat

import org.sfy.ttrip.data.remote.service.ChatApiService
import javax.inject.Inject

class ChatRemoteDataSourceImpl @Inject constructor(
    private val chatApiService: ChatApiService
) : ChatRemoteDataSource {

    override suspend fun getChatRooms(): List<ChatRoomResponse> =
        chatApiService.getChatRooms().data!!
}