package com.grigorevmp.habits.data.repository

import com.grigorevmp.habits.data.habit.HabitRefDao
import com.grigorevmp.habits.data.habit.HabitRefEntity
import com.grigorevmp.habits.data.habit.HabitType
import javax.inject.Inject

class HabitRefRepository @Inject constructor(private val habitRefDao: HabitRefDao) {
    fun insert(habit: HabitRefEntity) {
        habitRefDao.insert(habit)
    }

    suspend fun update(dateId: Long, habitId: Long, habitType: HabitType) {
        habitRefDao.update(dateId, habitId, habitType)
    }

    suspend fun update(dateId: Long, habitId: Long, habitType: HabitType, value: Int) {
        habitRefDao.update(dateId, habitId, habitType, value)
    }

    fun getHabitRefForDate(dateId: Long, habitId: Long) =
        habitRefDao.getHabitRefForDate(dateId, habitId)

    fun getAllHabitRefs(habitId: Long) =
        habitRefDao.getAllHabitRefs(habitId)
}