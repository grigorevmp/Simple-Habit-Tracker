package com.grigorevmp.habits.data.date

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.grigorevmp.habits.data.Converters
import java.time.LocalDate

@Entity(tableName = "date_table")
data class DateEntity(
    @PrimaryKey val id: Long? = null,
    @TypeConverters(Converters::class)
    @ColumnInfo val date: LocalDate,
)