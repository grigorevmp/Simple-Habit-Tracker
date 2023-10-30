package com.grigorevmp.habits.presentation.screen.today.data

import com.grigorevmp.habits.data.habit.HabitType

data class HabitEntityUI(
    val id: Long = 0,
    val dateId: Long,
    val title: String,
    val description: String,
    var type: HabitType,
)