package com.grigorevmp.habits.core.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.grigorevmp.habits.core.alarm.AlarmReceiver

object Utils {


    fun checkIfPendingIntentIsRegistered(context: Context, requestCode: Int): Boolean {
        val intent = Intent(context, AlarmReceiver::class.java)
        return PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE) != null
    }
}