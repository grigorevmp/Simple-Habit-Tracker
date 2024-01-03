package com.grigorevmp.habits.presentation.screen.today.data

import com.grigorevmp.habits.data.SerializableTimePickerState
import com.grigorevmp.habits.data.habit.HabitType

data class HabitEntityUI(
    val id: Long = 0,
    val dateId: Long,
    val title: String,
    val description: String,
    val category: String,
    val alert: Boolean,
    var time: SerializableTimePickerState,
    var type: HabitType,
    var countable: Boolean = false,
    var maxValue: Int? = null,
    var value: Int? = null,
    var valueName: String? = null,
    var valueAction: String? = null,
)