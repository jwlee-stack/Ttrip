package org.sfy.ttrip.domain.usecase.chat

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.domain.entity.chat.ChatRoom
import org.sfy.ttrip.domain.repository.chat.ChatRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetChatRoomsUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(): Resource<List<ChatRoom>> =
        withContext(Dispatchers.IO) {
            chatRepository.getChatRooms()
        }
}