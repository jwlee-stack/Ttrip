package org.sfy.ttrip.presentation.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okio.ByteString
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

    private val client = OkHttpClient()
    lateinit var webSocket: WebSocket
    val listener = WebSocketListener()

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

    fun connectSocket(chatroomId: Int, memberUuid: String, targetUuid: String) {
        val request = Request.Builder()
            .url("ws://k8d104.p.ssafy.io:8081/ws/chat/$chatroomId/$memberUuid/$targetUuid")
            .build()
        webSocket = client.newWebSocket(request, listener)
    }

    fun disconnectSocket() {
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
    }

    fun sendMessage(message: String) {
        webSocket.send(message)
    }

    inner class WebSocketListener : okhttp3.WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.d("Socket", "onOpen")
            super.onOpen(webSocket, response)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d("Socket", "Receiving : $text")
            val gson = Gson()
            val chatResponse = gson.fromJson(text, ChatDetail::class.java)
            _chatDetail.value.orEmpty().plus(chatResponse)
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            Log.d("Socket", "Receiving bytes : $bytes")
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            Log.d("Socket", "Closing : $code / $reason")
            webSocket.close(NORMAL_CLOSURE_STATUS, null)
            webSocket.cancel()
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.d("Socket", "Error : " + t.message)
        }
    }

    companion object {
        private const val NORMAL_CLOSURE_STATUS = 1000
    }
}