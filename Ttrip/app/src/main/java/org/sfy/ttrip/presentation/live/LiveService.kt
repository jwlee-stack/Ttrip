package org.sfy.ttrip.presentation.live

import android.app.*
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import okhttp3.*
import okio.ByteString
import org.json.JSONObject
import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.domain.entity.live.LiveUser
import org.sfy.ttrip.domain.usecase.live.GetLiveUsersUseCase
import javax.inject.Inject

@AndroidEntryPoint
class LiveService : Service(), CoroutineScope by CoroutineScope(Dispatchers.Default) {

    @Inject
    lateinit var getLiveUsersUseCase: GetLiveUsersUseCase
    private val NOTIFICATION_ID = 1
    private val CHANNEL_ID = "LiveChannel"

    private val client = OkHttpClient()
    lateinit var webSocket: WebSocket

    private val _liveUserList = MutableLiveData<List<LiveUser?>?>()
    val liveUserList: LiveData<List<LiveUser?>?> = _liveUserList
    val filteredLiveUserList: MutableLiveData<List<LiveUser?>?> = MutableLiveData()
    val listener = WebSocketListener()

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onDestroy() {
        super.onDestroy()
        // CoroutineScope 종료
        cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, createNotification())

        // WebSocket 연결
        connectWebSocket()

        return START_STICKY
    }

    private fun createNotificationChannel() {
        val serviceChannel = NotificationChannel(
            CHANNEL_ID,
            "Live Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(serviceChannel)
    }

    private fun createNotification(): Notification {
        val intent = Intent(this, LiveFragment::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Live Service")
            .setContentText("Live Service is running.")
            .setContentIntent(pendingIntent)
            .build()
    }

    private fun removeLiveUserById(id: String) {
        val currentList = _liveUserList.value.orEmpty().toMutableList()
        val userToRemove = currentList.find { it?.memberUuid == id }
        currentList.remove(userToRemove)
        _liveUserList.postValue(currentList)
    }

    private fun addLiveUser(user: LiveUser) {
        val currentList = _liveUserList.value?.toMutableList() ?: mutableListOf()
        if (currentList.none { it?.memberUuid == user.memberUuid }) {
            currentList.add(user)
            _liveUserList.postValue(currentList)
        }
    }

    private fun connectWebSocket() {
        val request = Request.Builder()
            .url("ws://k8d104.p.ssafy.io:8081/ws/live")
            .build()
        webSocket = client.newWebSocket(request, listener)
    }

    private fun disconnectWebSocket() {
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
    }

    fun getLiveUsers(city: String, lng: Double, lat: Double) = launch {
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