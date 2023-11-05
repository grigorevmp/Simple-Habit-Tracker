package com.grigorevmp.habits.core.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.grigorevmp.habits.core.utils.Utils
import com.grigorevmp.habits.data.HabitEntity
import java.util.Calendar

class AlarmScheduler {

    fun schedule(context: Context, item: HabitEntity) {
        val alarmManager = context.getSystemService(AlarmManager::class.java)

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("EXTRA_ID", item.id)
            putExtra("EXTRA_MESSAGE", item.title)
        }

        Log.d("Alarm manager", "==== Alarm manager block =====")

        for (dayOfWeek in item.days) {
            val calendar = Calendar.getInstance()
            calendar[Calendar.DAY_OF_WEEK] = dayOfWeek.value + 1
            calendar[Calendar.HOUR_OF_DAY] = item.time.hour
            calendar[Calendar.MINUTE] = item.time.minute
            calendar[Calendar.SECOND] = 0

            val requestCode = (item.id * 100).toInt() + dayOfWeek.ordinal

            Log.d("Alarm manager", "Registered alarm $requestCode for id=${item.title} on ${dayOfWeek.name} in ${item.time.hour}:${item.time.minute}")
            Log.d("Alarm manager", "Calendar format: ${calendar.time}")
            Log.d("Alarm manager", "Is alarm created: ${Utils.checkIfPendingIntentIsRegistered(context, requestCode)}")

            if (calendar.time < Calendar.getInstance().time) {
                Log.d("Alarm manager", "Past date. Increasing")

                calendar.add(Calendar.DATE, 7)

                Log.d("Alarm manager", "New date: Registered alarm $requestCode for id=${item.title} on ${dayOfWeek.name} in ${item.time.hour}:${item.time.minute}")
                Log.d("Alarm manager", "New date: Calendar format: ${calendar.time}")
            }

            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY * 7,
                PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
        }

        Log.d("Alarm manager", "==== =================== =====")
    }

    fun cancel(context: Context, item: HabitEntity) {
        val alarmManager = context.getSystemService(AlarmManager::class.java)

        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                item.hashCode(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}