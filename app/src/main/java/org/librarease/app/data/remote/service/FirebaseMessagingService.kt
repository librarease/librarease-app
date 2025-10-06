package org.librarease.app.data.remote.service
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.librarease.app.MainActivity
import org.librarease.app.R
import org.librarease.app.core.PushTokenWorker

class MyFirebaseMessagingService: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        PushTokenWorker.enqueue(this, token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        message.notification?.let {
            sendNotification(it.title ?: "", it.body ?: "")
        }
    }
    private fun sendNotification(title: String, body: String) {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val builder = NotificationCompat.Builder(this, "channel_id")
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.elegant_book_logo)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(0, builder.build())
    }
}