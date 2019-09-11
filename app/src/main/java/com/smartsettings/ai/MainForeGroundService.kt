package com.smartsettings.ai

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.smartsettings.ai.models.SmartProfile
import com.smartsettings.ai.repositories.SmartSettingRepository
import javax.inject.Inject


class MainForeGroundService : Service() {

    val CHANNEL_ID = "ForegroundServiceChannel"

    @Inject
    lateinit var smartSettingRepository: SmartSettingRepository

    companion object {
        fun startService(context: Context) {
            val intent = Intent(context, MainForeGroundService::class.java)
            context.startService(intent)
        }

        fun stopService(context: Context) {
            val intent = Intent(context, MainForeGroundService::class.java)
            context.stopService(intent)
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        showNotification()

        val smartSettingsPairs = SmartProfile.getSmartSettings()

        for (pair in smartSettingsPairs) {
            if (pair.first.isRunning() && !pair.second) {
                pair.first.stopListeningChanges()
            } else if (!pair.first.isRunning() && pair.second) {
                pair.first.listenForChanges()
            }
        }

        return START_STICKY
    }

    private fun showNotification() {

        createNotificationChannel()

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service")
            .setContentText("Running")
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }
}