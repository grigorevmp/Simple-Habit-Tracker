package com.grigorevmp.habits.presentation.screen.habits

import android.content.Context
import com.grigorevmp.habits.R
import com.grigorevmp.habits.data.SerializableTimePickerState
import java.time.DayOfWeek


fun Array<DayOfWeek>.parseToDate(context: Context): String {
    val daysList = this.toList()
    val daysListSorted = daysList.sortedBy { it.value }
    val daysListMapped = daysListSorted.map {
        when (it.value) {
            1 -> context.getString(R.string.day_1_short)
            2 -> context.getString(R.string.day_2_short)
            3 -> context.getString(R.string.day_3_short)
            4 -> context.getString(R.string.day_4_short)
            5 -> context.getString(R.string.day_5_short)
            6 -> context.getString(R.string.day_6_short)
            7 -> context.getString(R.string.day_7_short)
            else -> context.getString(R.string.day_7_short)
        }
    }

    return daysListMapped.joinToString(", ")
}

fun DayOfWeek.parseToDate(context: Context): String {
    return when (value) {
        1 -> context.getString(R.string.day_1_short)
        2 -> context.getString(R.string.day_2_short)
        3 -> context.getString(R.string.day_3_short)
        4 -> context.getString(R.string.day_4_short)
        5 -> context.getString(R.string.day_5_short)
        6 -> context.getString(R.string.day_6_short)
        7 -> context.getString(R.string.day_7_short)
        else -> context.getString(R.string.day_7_short)
    }
}

fun SerializableTimePickerState.toReadableForms(): String {

    val hour = if (hour > 9) hour.toString() else "0$hour"
    val minute = if (minute > 9) minute.toString() else "0$minute"

    return "$hour:$minute"
}