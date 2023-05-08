package org.sfy.ttrip.presentation.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.domain.entity.chat.ChatDetail
import org.sfy.ttrip.domain.entity.chat.ChatRoom
import org.sfy.ttrip.domain.usecase.chat.*
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getChatRoomsUseCase: GetChatRoomsUseCase,
    private val exitChatRoomUseCase: ExitChatRoomUseCase,
    private val getChatDetailUseCase: GetChatDetailUseCase,
    private val createChatRoomUseCase: CreateChatRoomUseCase,
    private val chatMatchUseCase: ChatMatchUseCase
) : ViewModel() {

    private val _chatRooms: MutableLiveData<List<ChatRoom>?> = MutableLiveData()
    val chatRooms: LiveData<List<ChatRoom>?> = _chatRooms

    private val _chatDetail: MutableLiveData<List<ChatDetail>?> = MutableLiveData()
    val chatDetail: LiveData<List<ChatDetail>?> = _chatDetail

    var chatId = 0

    fun getChatRooms() {
        viewModelScope.launch {
            when (val value = getChatRoomsUseCase()) {
                is Resource.Success -> {
                    _chatRooms.value = value.data
                }
                is Resource.Error -> {
                    Log.d("getChatRooms", "getChatRooms: ${value.errorMessage}")
                }
            }
        }
    }

    fun exitChatRoom(chatId: Int) {
        viewModelScope.launch { exitChatRoomUseCase(chatId) }
    }

    fun getChatDetail(chatId: Int) {
        viewModelScope.launch {
            when (val value = getChatDetailUseCase(chatId)) {
                is Resource.Success -> {
                    _chatDetail.value = value.data
                }
                is Resource.Error -> {
                    Log.d("getChatDetail", "getChatDetail: ${value.errorMessage}")
                }
            }
        }
    }

    fun createChatRoom(articleId: Int, opponentUserUuid: String) {
        viewModelScope.launch {
            createChatRoomUseCase(articleId, opponentUserUuid)
        }
    }

    fun chatMatch(articleId: Int, opponentUserUuid: String) {
        viewModelScope.launch { chatMatchUseCase(articleId, opponentUserUuid) }
    }
}