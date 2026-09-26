package com.newstelugu.app.core.service

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.newstelugu.app.core.database.AppDatabase
import com.newstelugu.app.core.database.entity.NotificationEntity
import com.newstelugu.app.presentation.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewsTeluguMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Send updated token to backend in background
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val title = remoteMessage.notification?.title ?: remoteMessage.data["title"] ?: "NewsTelugu Alert"
        val message = remoteMessage.notification?.body ?: remoteMessage.data["message"] ?: ""
        val type = remoteMessage.data["type"] ?: "NEUTRAL"
        val articleId = remoteMessage.data["article_id"]
        val stockSymbol = remoteMessage.data["stock_symbol"]

        // Save to Room DB
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(applicationContext)
            db.notificationDao().insertNotification(
                NotificationEntity(
                    type = type,
                    title = title,
                    message = message,
                    articleId = articleId,
                    stockSymbol = stockSymbol,
                    isRead = false,
                    createdAt = System.currentTimeMillis().toString()
                )
            )
        }

        showNotification(title, message, type, articleId, stockSymbol)
    }

    private fun showNotification(
        title: String,
        message: String,
        type: String,
        articleId: String?,
        stockSymbol: String?
    ) {
        val channelId = "newstelugu_alerts_channel"
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "NewsTelugu Financial Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Categorized financial news, market, and technical alerts"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("NOTIFICATION_TYPE", type)
            putExtra("ARTICLE_ID", articleId)
            putExtra("STOCK_SYMBOL", stockSymbol)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }
}
