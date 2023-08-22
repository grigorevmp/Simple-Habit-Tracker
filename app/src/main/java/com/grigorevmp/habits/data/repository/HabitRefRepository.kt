package com.grigorevmp.habits.data.repository

import com.grigorevmp.habits.data.HabitEntity
import com.grigorevmp.habits.data.HabitWithDateDao
import com.grigorevmp.habits.data.habit.HabitRefDao
import com.grigorevmp.habits.data.habit.HabitRefEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class HabitRefRepository @Inject constructor(private val habitRefDao: HabitRefDao) {
    suspend fun insert(habit: HabitRefEntity) {
        habitRefDao.insert(habit)
    }
}