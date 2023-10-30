package com.grigorevmp.habits.core.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.grigorevmp.habits.R
import com.grigorevmp.habits.presentation.screen.common.MainActivity

private const val NOTIFICATION_ID = 33
const val CHANNEL_ID = "ReminderChannel"

fun createChannel(context: Context) {
    val notificationChannel =
        NotificationChannel(CHANNEL_ID, "Habit reminder", NotificationManager.IMPORTANCE_HIGH)
    val notificationManager = context.getSystemService(NotificationManager::class.java)
    notificationManager.createNotificationChannel(notificationChannel)
}

fun NotificationManager.sendReminderNotification(
    context: Context,
    title: String,
    message: String
) {
    val contentIntent = Intent(context, MainActivity::class.java)
    val contentPendingIntent = PendingIntent.getActivity(
        context,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setContentTitle(title)
        .setContentText(message)
        .setStyle(
            NotificationCompat.BigTextStyle()
                .bigText(message)
        )
        .setSmallIcon(R.drawable.ic_task)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        .setContentIntent(contentPendingIntent)
        .build()

    this.notify(NOTIFICATION_ID, builder)
}