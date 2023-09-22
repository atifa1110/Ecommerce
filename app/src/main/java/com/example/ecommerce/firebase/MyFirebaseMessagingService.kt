package com.example.ecommerce.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.ecommerce.MainActivity
import com.example.ecommerce.R
import com.example.ecommerce.room.notification.Notification
import com.example.ecommerce.room.notification.NotificationRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService(
) {

    @Inject
    lateinit var repository : NotificationRepository


    //Called when message is received and app is in foreground.
    private val TAG = "FirebaseMessagingService"

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//        val body = remoteMessage.notification?.body.toString()
//        val title = remoteMessage.notification?.title.toString()
//        val image = remoteMessage.notification?.imageUrl.toString()
        val body = remoteMessage.data["body"] ?: ""
        val title = remoteMessage.data["title"] ?: ""
        val image = remoteMessage.data["image"] ?: ""
        val type = remoteMessage.data["type"] ?: ""
        val date = remoteMessage.data["date"] ?: ""
        val time = remoteMessage.data["time"] ?: ""

        sendNotification(title,body,image,type,date,time)
        //Check if message contains a data payload.
        remoteMessage.data.let {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)

        }

        //Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")

        }
    }

    private fun getBitmapFromURL(image: String): Bitmap? {
        return try {
            val url = URL(image)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val inputStream = connection.inputStream
            BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
    private fun sendNotification(title:String,body:String,image:String,type:String,date:String,time:String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(FLAG_ACTIVITY_CLEAR_TOP)
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, FLAG_IMMUTABLE
        )

        repository.addNotification(Notification(title = title,
            body = body, image = image, type = type, date = date, time = time, isRead = false))

        val channelId = this.getString(R.string.default_notification_channel_id)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        if(image!=null){
            notificationBuilder.setLargeIcon(getBitmapFromURL(image))
        }

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "My Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(channelId, channelName, importance)
            manager.createNotificationChannel(channel)
        }

        manager.notify(1, notificationBuilder.build())
    }
}