package org.sfy.ttrip.data.remote.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.sfy.ttrip.MainActivity
import org.sfy.ttrip.R
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class FirebaseService : FirebaseMessagingService() {

    private lateinit var intent: Intent

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(ContentValues.TAG, "Refreshed token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if (remoteMessage.data.isNotEmpty()) {
            val type = remoteMessage.data["body"]
            Log.d("check remoteMessage", "onMessageReceived: $type")
            Log.d("check remoteMessage", remoteMessage.data.toString())
            Log.d("check remoteMessage", remoteMessage.from.toString())
            Log.d("check remoteMessage", remoteMessage.notification!!.body.toString())

            sendNotificationForeground(remoteMessage.notification!!.body.toString(), remoteMessage)
        }
    }

    private fun sendNotificationForeground(notificationBody: String, remoteMessage: RemoteMessage) {
        val type = remoteMessage.data["type"]!!.toInt()
        val nickName = remoteMessage.data["nickName"]
        val memberUuid = remoteMessage.data["memberUuid"]

        when (type) {
            0 -> {
                intent = Intent(this, MainActivity::class.java)
            }
            1 -> {
                val result = remoteMessage.data["result"]
                intent = Intent(this, MainActivity::class.java)
            }
            2 -> {
                val articleId = remoteMessage.data["articleId"]
                val dDay = remoteMessage.data["dDay"]
                intent = Intent(this, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    putExtra("fragment", "BoardFragment")
                    putExtra("articleId", articleId)
                    putExtra("dDay", dDay)
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            3 -> {
                val chatroomId = remoteMessage.data["chatroomId"]
                intent = Intent(this, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    putExtra("fragment", "ChatFragment")
                    putExtra("chatroomId", chatroomId)
                }
            }
            4 -> {
                val matchHistoryId = remoteMessage.data["matchHistoryId"]
                intent = Intent(this, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    putExtra("fragment", "evaluateDialog")
                    putExtra("nickName", nickName)
                    putExtra("matchHistoryId", matchHistoryId)
                }
            }
            5 -> {
                intent = Intent(this, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    putExtra("fragment", "mMyPageFragment")
                }
            }
            else -> {
                intent = Intent(this, MainActivity::class.java)
            }
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val CHANNEL_ID = getString(R.string.notification_channel_id)
        val CHANNEL_NAME = getString(R.string.notification_channel_name)
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_app_logo_round)
            .setContentText(notificationBody)
            .setAutoCancel(true)
            .setSound(soundUri)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)

        notificationManager.notify(0, notificationBuilder.build())
    }

    suspend fun getCurrentToken() = suspendCoroutine<String> { continuation ->
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d(ContentValues.TAG, token)
                continuation.resume(token)
            } else {
                Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
                continuation.resume("")
                return@OnCompleteListener
            }
        })
    }
}