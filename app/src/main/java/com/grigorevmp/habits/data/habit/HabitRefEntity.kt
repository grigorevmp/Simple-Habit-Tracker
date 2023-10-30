package com.grigorevmp.habits.data.habit

import androidx.room.Entity

@Entity(tableName = "habit_ref", primaryKeys = ["habitId", "dateId"])
data class HabitRefEntity(
    val habitId: Long,
    val dateId: Long,
    val habitType: HabitType = HabitType.Missed
)

enum class HabitType {
    Done,
    Unknown,
    Missed
}