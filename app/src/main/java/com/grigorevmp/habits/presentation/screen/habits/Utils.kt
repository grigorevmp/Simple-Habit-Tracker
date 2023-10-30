package com.grigorevmp.habits.presentation.screen.habits

import com.grigorevmp.habits.data.SerializableTimePickerState
import java.time.DayOfWeek


fun Array<DayOfWeek>.parseToDate(): String {
    val daysList = this.toList()
    val daysListSorted = daysList.sortedBy { it.value }
    val daysListMapped= daysListSorted.map {
        when (it.value) {
            1 -> "Пн"
            2 -> "Вт"
            3 -> "Ср"
            4 -> "Чт"
            5 -> "Пт"
            6 -> "Сб"
            7 -> "Вс"
            else -> "Вс"
        }
    }

    return daysListMapped.joinToString(", ")
}

fun SerializableTimePickerState.toReadableForms(): String {

    val hour = if (hour > 9) hour.toString() else "0$hour"
    val minute = if (minute > 9) minute.toString() else "0$minute"

    return "$hour:$minute"
}