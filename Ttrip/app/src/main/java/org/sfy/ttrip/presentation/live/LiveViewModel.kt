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
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okio.ByteString
import org.json.JSONObject
import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.domain.usecase.live.GetLiveUsersUseCase

@HiltViewModel
class LiveViewModel @Inject constructor(
    private val getLiveUsersUseCase: GetLiveUsersUseCase
) : ViewModel() {

    private val _liveUserList = MutableLiveData<List<LiveUser?>?>()
    val liveUserList: LiveData<List<LiveUser?>?> = _liveUserList

    private val client = OkHttpClient()

    lateinit var webSocket: WebSocket

    val filteredLiveUserList: MutableLiveData<List<LiveUser?>?> = MutableLiveData()
    val listener = WebSocketListener()
    val liveOn: MutableLiveData<Boolean> = MutableLiveData(false)

    var cityOnLive: MutableLiveData<String?> = MutableLiveData("")
    var lng = 0.0
    var lat = 0.0
    var lastUpdateTime = 0L

    private fun removeLiveUserById(id: String) {
        val currentList = _liveUserList.value.orEmpty().toMutableList()
        val userToRemove = currentList.find { it?.memberUuid == id }
        currentList.remove(userToRemove)
        _liveUserList.value = currentList
    }

    private fun addLiveUser(user: LiveUser) {
        val currentList = _liveUserList.value?.toMutableList() ?: mutableListOf()
        currentList.add(user)
        _liveUserList.value = currentList.toList()
    }

    fun connectSocket(city: String, memberId: String) {
        val request = Request.Builder()
            .url("ws://k8d104.p.ssafy.io:8081/ws/live/$city/$memberId")
            .build()
        webSocket = client.newWebSocket(request, listener)
    }

    fun disconnectSocket() {
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
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

    fun sendMyInfo(
        city: String,
        uuid: String,
        lat: Double,
        lng: Double,
        nickname: String,
        gender: String,
        age: String,
        profileImgPath: String,
        markerImgPath: String
    ) {
        val data = JSONObject().apply {
            put("city", city)
            put("memberUuid", uuid)
            put("latitude", lat)
            put("longitude", lng)
            put("nickname", nickname)
            put("gender", gender)
            put("age", age)
            put("profileImgPath", profileImgPath)
            put("markerImgPath", markerImgPath)
        }
        webSocket.send(data.toString())

    }

    inner class WebSocketListener : okhttp3.WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.d("Socket", "onOpen")
            super.onOpen(webSocket, response)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d("Socket", "Receiving : $text")
            val gson = Gson()
            val userResponse = gson.fromJson(text, LiveUser::class.java)
            if (userResponse.latitude.toInt() == -1 && userResponse.longitude.toInt() == -1) {
                removeLiveUserById(userResponse.memberUuid)
            } else {
                addLiveUser(
                    LiveUser(
                        userResponse.nickname,
                        userResponse.gender,
                        userResponse.age,
                        userResponse.memberUuid,
                        userResponse.latitude,
                        userResponse.longitude,
                        userResponse.matchingRate,
                        userResponse.matchingRate
                    )
                )
            }
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