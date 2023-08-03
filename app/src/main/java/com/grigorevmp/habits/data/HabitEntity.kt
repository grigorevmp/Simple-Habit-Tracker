package com.grigorevmp.habits.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habit_table")
data class HabitEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "habit_title") val title: String,
    @ColumnInfo(name = "habit_description") val description: String,
    @ColumnInfo(name = "habit_completed") val completed: Boolean = false,
)