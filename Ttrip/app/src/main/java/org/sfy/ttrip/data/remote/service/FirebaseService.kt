package org.sfy.ttrip.data.remote.service

import android.app.NotificationChannel
import android.app.NotificationManager
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
            val type = remoteMessage.data["type"]
            //val body = remoteMessage.

//            if (type == 2) {
//
//            } else if (type == 3) {
//
//            }

        } else if (remoteMessage.notification != null) {

        }
    }

    private fun sendNotificationForeground(type: Int) {
        intent = when (type) {
            2 -> {
                Intent(this, MainActivity::class.java)
            }
            3 -> {
                Intent(this, MainActivity::class.java)
            }
            else -> {
                Intent(this, MainActivity::class.java)
            }
        }


//        val CHANNEL_ID = getString(R.string.notification_channel_id)
//        val CHANNEL_NAME = getString(R.string.notification_channel_name)
//        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
//            .setSmallIcon(R.mipmap.ic_app_logo_round)
//            .setContentText(body)
//            .setAutoCancel(true)
//            .setSound(soundUri)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setContentIntent(pendingIntent)
//        val notificationManager =
//            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        val channel = NotificationChannel(
//            CHANNEL_ID,
//            CHANNEL_NAME,
//            NotificationManager.IMPORTANCE_HIGH
//        )
//        notificationManager.createNotificationChannel(channel)
//
//        notificationManager.notify(0, notificationBuilder.build())
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