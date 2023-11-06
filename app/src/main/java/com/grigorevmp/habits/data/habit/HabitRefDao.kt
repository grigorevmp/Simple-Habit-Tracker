package com.grigorevmp.habits.data.habit

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HabitRefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(habitRefEntity: HabitRefEntity)

    @Delete
    fun delete(habitRefEntity: HabitRefEntity)

    @Query("UPDATE habit_ref SET habitType = :habitType WHERE dateId = :dateId AND habitId = :habitId")
    suspend fun update(dateId: Long, habitId: Long, habitType: HabitType)

    @Query("UPDATE habit_ref SET habitType = :habitType, value = :value WHERE dateId = :dateId AND habitId = :habitId")
    suspend fun update(dateId: Long, habitId: Long, habitType: HabitType, value: Int)

    @Query("SELECT * FROM habit_ref WHERE dateId = :dateId AND habitId = :habitId")
    fun getHabitRefForDate(dateId: Long, habitId: Long): HabitRefEntity?
}