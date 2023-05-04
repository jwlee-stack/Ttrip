package org.sfy.ttrip.presentation.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.domain.entity.chat.ChatRoom
import org.sfy.ttrip.domain.usecase.chat.ExitChatRoomUseCase
import org.sfy.ttrip.domain.usecase.chat.GetChatRoomsUseCase
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getChatRoomsUseCase: GetChatRoomsUseCase,
    private val exitChatRoomUseCase: ExitChatRoomUseCase
) : ViewModel() {

    private val _chatRooms: MutableLiveData<List<ChatRoom>?> = MutableLiveData()
    val chatRooms: LiveData<List<ChatRoom>?> = _chatRooms

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
}