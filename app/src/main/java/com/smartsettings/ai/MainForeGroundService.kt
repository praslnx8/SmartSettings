package com.smartsettings.ai

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.Observer
import com.smartsettings.ai.models.SmartProfile
import com.smartsettings.ai.models.smartSettings.SmartSetting
import com.smartsettings.ai.repositories.SmartSettingRepository
import javax.inject.Inject


class MainForeGroundService : LifecycleService() {

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

    override fun onCreate() {
        super.onCreate()

        SmartProfile.getSmartSettingLiveData()
            .observe(this, Observer<Set<SmartSetting<out Any, out Any, out Any>>> { smartSettings ->
                smartSettings.forEach { smartSetting ->
                    Log.d("XDFCE", "service refreshed")
                    if (smartSetting.isEnabled()) {
                        smartSetting.start()
                    } else {
                        smartSetting.stop()
                    }
                }
            })
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        showNotification()

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
            .setSound(null)
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
            manager?.createNotificationChannel(serviceChannel)
        }
    }
}