package com.grigorevmp.habits.core.alarm

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra("EXTRA_MESSAGE") ?: return

        val notificationManager =
            ContextCompat.getSystemService(
                context!!,
                NotificationManager::class.java
            ) as NotificationManager

        notificationManager.sendReminderNotification(
            context = context,
            title = "Habit reminder",
            message = message
        )
    }
}