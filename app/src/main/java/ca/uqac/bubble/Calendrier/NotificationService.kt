package ca.uqac.bubble.Calendrier

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder

class NotificationService : Service() {

    private val notificationScheduler = NotificationScheduler()

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        val intentFilter = IntentFilter().apply {
            addAction(Intent.ACTION_DATE_CHANGED)
        }
        registerReceiver(notificationScheduler, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(notificationScheduler)
    }
}