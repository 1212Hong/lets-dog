package com.dog.util.common

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.dog.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d("FCM Log", "Refreshed token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("메시지 내용", "Message data payload: ${remoteMessage.data}")
        if (remoteMessage.data.isNotEmpty()) {
            val title = remoteMessage.data["title"]
            val content = remoteMessage.data["content"]
            showNotification(title, content)
            Log.d("메시지 결과", "content 값 : $content")
        }
    }

    private fun showNotification(title: String?, content: String?) {
        Log.d("showNotification", "content 값 : $content")

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d("버젼 확인", "content 값 : ${Build.VERSION.SDK_INT}")

            createNotificationChannel(notificationManager)
        }

        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(content)
            .setContentInfo("Info")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

//        notificationManager.notify(Random().nextInt(), notificationBuilder.build())

        notificationManager.notify(1, notificationBuilder.build())


    }

    private fun createNotificationChannel(manager: NotificationManager) {

        val notificationChannel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationChannel.description = NOTIFICATION_CHANNEL_DESCRIPTION
        manager.createNotificationChannel(notificationChannel)
    }

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "dog"
        private const val NOTIFICATION_CHANNEL_NAME = "Notification"
        private const val NOTIFICATION_CHANNEL_DESCRIPTION = "notification channel"
    }
}