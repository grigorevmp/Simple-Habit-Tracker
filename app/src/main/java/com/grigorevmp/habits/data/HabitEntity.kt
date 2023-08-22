package com.grigorevmp.habits.data

import android.icu.util.LocaleData
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.grigorevmp.habits.data.habit.HabitRefEntity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.time.LocalTime
import java.util.Collections

@Entity(tableName = "habit_table")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "habit_title") val title: String,
    @ColumnInfo(name = "habit_description") val description: String,
    @ColumnInfo(name = "habit_completed") val completed: Boolean = false,
)

data class HabitWithDates(
    @Embedded val habit: HabitEntity,
    @TypeConverters(Converters::class)
    @Relation(
        parentColumn = "id",
        entityColumn = "habitId",
        entity = HabitRefEntity::class,
        projection = ["dateId"],
    ) val dateIdList: MutableList<Long>
)


class Converters {

    @TypeConverter
    fun toHabitRefEntity(data: String?): MutableList<Long?>? {
        if (data == null) {
            return Collections.emptyList()
        }
        return Json.decodeFromString(data)
    }

    @TypeConverter
    fun fromHabitRefEntity(someObjects: MutableList<Long?>?): String {
        return Json.encodeToString(someObjects)
    }

    @TypeConverter
    fun toLocalDate(data: Long?): LocalDate? {
        if (data == null) {
            return LocalDate.now()
        }
        return LocalDate.ofEpochDay(data)
    }

    @TypeConverter
    fun fromHLocalDate(someObjects: LocalDate?): Long {
        return someObjects?.toEpochDay() ?: LocalDate.now().toEpochDay()
    }
}