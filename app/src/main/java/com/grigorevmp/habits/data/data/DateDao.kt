package com.grigorevmp.habits.data.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.time.LocalDate

@Dao
interface DateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(date: DateEntity)

    @Delete
    fun delete(date: DateEntity)

    @Query("SELECT * FROM date_table WHERE date = :date")
    fun getDateId(date: LocalDate): DateEntity?
}
