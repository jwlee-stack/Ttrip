package org.sfy.ttrip.presentation.live

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dds.skywebrtc.inter.ISkyEvent
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okio.ByteString
import org.json.JSONObject
import org.sfy.ttrip.ApplicationClass
import org.sfy.ttrip.domain.entity.live.CallItem
import org.sfy.ttrip.domain.usecase.live.CreateSessionUseCase
import org.sfy.ttrip.domain.usecase.live.GetCallTokenUseCase
import org.sfy.ttrip.domain.usecase.live.GetLiveUsersUseCase
import javax.inject.Inject

@HiltViewModel
class CallViewModel @Inject constructor(
    private val getLiveUsersUseCase: GetLiveUsersUseCase,
    private val createSessionUseCase: CreateSessionUseCase,
    private val getCallTokenUseCase: GetCallTokenUseCase
) : ViewModel() {

    private val _callData: MutableLiveData<CallItem?> = MutableLiveData()
    val callData: LiveData<CallItem?> = _callData

    private val client = OkHttpClient()

    lateinit var webSocket: WebSocket

    val listener = WebSocketListener()

//    suspend fun createCallingSession() = viewModelScope.async {
//        when (val value = createSessionUseCase()) {
//            is Resource.Success -> {
//                _sessionId.value = value.data.sessionId
//                return@async _sessionId.value
//            }
//            is Resource.Error -> {
//                Log.d("createCallingSession", "createCallingSession: ${value.errorMessage}")
//                return@async 0
//            }
//        }
//    }.await()

    fun connectSocket(memberId: String) {
        val request = Request.Builder()
            .url("ws://k8d104.p.ssafy.io:8081/ws/call/$memberId")
//            .url("ws://192.168.132.6:5000/ws/$memberId")
//            .url("ws://192.168.25.22:5000/ws/$memberId")
            .build()
        webSocket = client.newWebSocket(request, listener)
    }

    fun disconnectSocket() {
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
    }

    fun sendMyInfo(
        type: String,
        sessionId: String,
        memberId: String,
        otherId: String
    ) {
        val data = JSONObject().apply {
            put("type", type)
            put("sessionId", sessionId)
            put("memberUuid", memberId)
            put("otherUuid", otherId)
        }
        webSocket.send(data.toString())
    }

    inner class WebSocketListener : okhttp3.WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.d("CallSocket", "onOpen")
            super.onOpen(webSocket, response)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d("CallSocket", "Receiving : $text")
            val gson = Gson()
            val callResponse = gson.fromJson(text, CallItem::class.java)
            // 전화 발신

            _callData.value = callResponse
//            if(callResponse.type.equals("__invite")) {
//                Log.d("CallTest", "re")
//            }
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            Log.d("CallSocket", "Receiving bytes : $bytes")
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            Log.d("CallSocket", "Closing : $code / $reason")
            webSocket.close(NORMAL_CLOSURE_STATUS, null)
            webSocket.cancel()
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.d("CallSocket", "Error : " + t.message)
        }
    }

    companion object {
        private const val NORMAL_CLOSURE_STATUS = 1000
    }

}