package com.grigorevmp.habits.data.habit

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

@Entity(primaryKeys = ["habitId", "dateId"])
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