package com.smartsettings.ai.runner

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LifecycleService
import com.smartsettings.ai.utils.AndroidNotificationUtil


class MainForeGroundService : LifecycleService() {

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

        showNotification()

        MainService.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()

        MainService.onDestroy()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        return START_STICKY

    }

    private fun showNotification() {

        val title = "Foreground Service"
        val text = "Running"
        val channelId = "ForegroundServiceChannel"

        startForeground(1, AndroidNotificationUtil.getNotification(this, title, text, channelId))
    }
}