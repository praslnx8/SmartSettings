package com.smartsettings.ai.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.smartsettings.ai.MainActivity
import com.smartsettings.ai.R

object AndroidNotificationUtil {

    private fun createNotificationChannel(context: Context, channelId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                channelId,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager = context.getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }

    fun getNotification(context: Context, title: String, text: String, channelId: String) : Notification {
        createNotificationChannel(context, channelId)

        val notificationIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0, notificationIntent, 0
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentIntent(pendingIntent)
            .setSound(null)
            .build()

        return notification
    }

    fun showNotification(context: Context, title: String, text: String, channelId: String) {

        val manager = context.getSystemService(NotificationManager::class.java)
        manager?.notify(2, getNotification(context, title, text, channelId))
    }
}