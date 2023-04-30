package org.sfy.ttrip.presentation.live

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.OkHttpClient
import okhttp3.Request
import org.sfy.ttrip.domain.entity.live.LiveUser
import javax.inject.Inject
import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okio.ByteString
import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.domain.usecase.live.GetLiveUsersUseCase

@HiltViewModel
class LiveViewModel @Inject constructor(
    private val getLiveUsersUseCase: GetLiveUsersUseCase
) : ViewModel() {

    private val _liveUserList = MutableLiveData<List<LiveUser?>?>()
    val liveUserList: LiveData<List<LiveUser?>?> = _liveUserList

    val filteredLiveUserList: MutableLiveData<List<LiveUser?>?> = MutableLiveData()

    private val client = OkHttpClient()
    private val request = Request.Builder()
        .url("http://k8d104.p.ssafy.io:8081")
        .build()

    val liveOn: MutableLiveData<Boolean> = MutableLiveData(false)
    var cityOnLive: MutableLiveData<String?> = MutableLiveData("")
    var lng = 0.0
    var lat = 0.0
    var lastUpdateTime = 0L

    init {
        val listener = WebSocketListener()
        client.newWebSocket(request, listener)
    }

    fun getLiveUsers(city: String, lng: Double, lat: Double) = viewModelScope.launch {
        when (val value = getLiveUsersUseCase(city, lng, lat)) {
            is Resource.Success -> {
                _liveUserList.value = value.data
            }
            is Resource.Error -> {
                Log.d("getLiveUsers", "getLiveUsers: ${value.errorMessage}")
            }
        }
    }

    fun setLiveUserReset() {
        _liveUserList.value = null
    }

    inner class WebSocketListener : okhttp3.WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            webSocket.send("{\"type\":\"ticker\", \"symbols\": [\"BTC_KRW\"], \"tickTypes\": [\"30M\"]}")
            webSocket.close(NORMAL_CLOSURE_STATUS, null) //없을 경우 끊임없이 서버와 통신함
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d("Socket", "Receiving : $text")
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