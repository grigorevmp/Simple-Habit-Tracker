package com.grigorevmp.habits.data.repository

import com.grigorevmp.habits.data.HabitEntity
import com.grigorevmp.habits.data.HabitWithDateDao
import com.grigorevmp.habits.data.HabitWithDates
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class HabitRepository @Inject constructor(private val habitWithDateDao: HabitWithDateDao) {
    val allHabits: Flow<List<HabitWithDates>> = habitWithDateDao.getAllHabits()

    suspend fun insert(habit: HabitEntity) {
        habitWithDateDao.insert(habit)
    }

    suspend fun update(habit: HabitEntity) {
        habitWithDateDao.update(habit)
    }

    suspend fun delete(habit: HabitEntity) {
        habitWithDateDao.delete(habit)
    }

    fun getHabitForDate(dateId: Long, habitId: Long) = habitWithDateDao.getHabitForDate(dateId, habitId)
}