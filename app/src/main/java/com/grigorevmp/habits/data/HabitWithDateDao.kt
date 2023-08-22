package com.grigorevmp.habits.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitWithDateDao {
    @Transaction
    @Query("SELECT * FROM habit_table")
    fun getAllHabits(): Flow<List<HabitWithDates>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(habit: HabitEntity): Long

    @Update
    suspend fun update(habit: HabitEntity)

    @Delete
    suspend fun delete(habit: HabitEntity)

    @Query("SELECT * FROM habit_table WHERE id IN (SELECT habitId FROM HabitRefEntity WHERE dateId = :dateId AND habitId = :habitId)")
    fun getHabitForDate(dateId: Long, habitId: Long): HabitEntity?

}