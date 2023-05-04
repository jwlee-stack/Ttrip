package org.sfy.ttrip.data.remote.repository

import org.sfy.ttrip.common.util.wrapToResource
import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.data.remote.datasorce.chat.ChatRemoteDataSource
import org.sfy.ttrip.data.remote.datasorce.chat.ExitChatRequest
import org.sfy.ttrip.domain.entity.chat.ChatRoom
import org.sfy.ttrip.domain.repository.chat.ChatRepository
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatRemoteDataSource: ChatRemoteDataSource
) : ChatRepository {
    override suspend fun getChatRooms(): Resource<List<ChatRoom>> = wrapToResource {
        chatRemoteDataSource.getChatRooms().map { it.toDomainModel() }
    }

    override suspend fun exitChatRoom(chatId: Int): Boolean =
        chatRemoteDataSource.exitChatRoom(ExitChatRequest(chatId))
}