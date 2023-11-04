package com.grigorevmp.habits.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitWithDateDao {
    @Query("SELECT * FROM habit_table")
    fun getAllHabits(): Flow<MutableList<HabitWithDates>>

    @Query("SELECT * FROM habit_table")
    fun getAllOnlyHabits(): Flow<MutableList<HabitEntity>>

    @Query("SELECT * FROM habit_table where habit_deleted=0 AND habit_alert=1")
    fun getAllHabitsWithAlerts(): Flow<MutableList<HabitEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(habit: HabitEntity): Long

    @Update
    suspend fun update(habit: HabitEntity)
//
//    @Delete
//    suspend fun delete(habit: HabitEntity)
    @Query("UPDATE habit_table SET habit_deleted = 1 WHERE id = :habitId")
    suspend fun delete(habitId: Long)


    @Query("SELECT * FROM habit_table WHERE id IN (SELECT habitId FROM habit_ref WHERE dateId = :dateId AND habitId = :habitId)")
    fun getHabitForDate(dateId: Long, habitId: Long): HabitEntity?

    @Query("SELECT * FROM habit_table WHERE id = :habitId")
    fun getHabitById(habitId: Long): HabitEntity?

}