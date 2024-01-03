package com.grigorevmp.habits.data.date

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface DateDao {
    @Query("SELECT * FROM date_table")
    fun getAllDates(): Flow<MutableList<DateEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(date: DateEntity)

    @Delete
    fun delete(date: DateEntity)

    @Query("SELECT * FROM date_table WHERE date = :date")
    fun getDateId(date: LocalDate): DateEntity?

    @Query("SELECT * FROM date_table WHERE id = :dateId")
    fun getDate(dateId: Long): DateEntity?
}
