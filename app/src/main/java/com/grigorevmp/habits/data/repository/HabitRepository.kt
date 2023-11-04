package com.grigorevmp.habits.data.repository

import com.grigorevmp.habits.data.HabitEntity
import com.grigorevmp.habits.data.HabitWithDateDao
import javax.inject.Inject

class HabitRepository @Inject constructor(private val habitWithDateDao: HabitWithDateDao) {
    fun fetchHabits() = habitWithDateDao.getAllHabits()

    fun fetchOnlyHabits() = habitWithDateDao.getAllOnlyHabits()

    fun fetchHabitsWithAlerts() = habitWithDateDao.getAllHabitsWithAlerts()

    suspend fun insert(habit: HabitEntity): Long {
        return habitWithDateDao.insert(habit)
    }

    suspend fun update(habit: HabitEntity) {
        habitWithDateDao.update(habit)
    }

    suspend fun delete(habit: HabitEntity) {
        habitWithDateDao.delete(habit.id)
    }

    fun getHabitForDate(dateId: Long, habitId: Long) =
        habitWithDateDao.getHabitForDate(dateId, habitId)

    fun getHabitById(habitId: Long) = habitWithDateDao.getHabitById(habitId)
}