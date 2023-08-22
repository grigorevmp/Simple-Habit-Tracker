package com.grigorevmp.habits.data.habit

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface HabitRefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(habitRefEntity: HabitRefEntity)

    @Delete
    fun delete(habitRefEntity: HabitRefEntity)
}