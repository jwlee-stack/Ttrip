package org.sfy.ttrip.domain.usecase.chat

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.sfy.ttrip.domain.repository.chat.ChatRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExitChatRoomUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(chatId: Int): Boolean =
        withContext(Dispatchers.IO) {
            chatRepository.exitChatRoom(chatId)
        }
}