package com.grigorevmp.habits.data

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.grigorevmp.habits.data.habit.HabitRefEntity
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.DayOfWeek
import java.time.LocalDate

@Entity(tableName = "habit_table")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "habit_title") var title: String,
    @ColumnInfo(name = "habit_description") var description: String,
    @TypeConverters(Converters::class) @ColumnInfo(name = "habit_days") var days: Array<DayOfWeek>,
    @ColumnInfo(name = "habit_alert") var alertEnabled: Boolean,
    @ColumnInfo(name = "habit_time") var time: SerializableTimePickerState,
    @ColumnInfo(name = "habit_category") var habitCategory: HabitCategory = HabitCategory.None,
    @ColumnInfo(name = "habit_completed") val completed: Boolean = false,
    @ColumnInfo(name = "habit_deleted") val deleted: Boolean = false,
    @ColumnInfo(name = "habit_countable") var countable: Boolean = false,
    @ColumnInfo(name = "habit_countable_entity") var countableEntity: CountableEntity? = null,
) {

    fun representForBackup(): String = "$title. $description. ${days.joinToString { it.name }}"
}

enum class HabitCategory {
    None,
    Food,
    PhysicalActivity,
    Relaxation,
    Meditation,
    BadHabits,
    Reading,
    Education,
    Languages,
    Skills,
    Planning,
    Working,
    Diary,
    StressFighting,
    Communication,
    SelfTime,
    Productivity,
    WorkBalance,
    FinanceControl,
    Budget,
    Hobby,
    Cleaning,
    Cooking,
    PetTime,
    Volunteering,
    Personal,
    FriendsTime;
}

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

@Serializable
data class SerializableTimePickerState(
    val hour: Int,
    val minute: Int
)

@Serializable
data class CountableEntity(
    val targetValue: Int,
    val valueName: String,
    val actionName: String,
) {

    fun representForBackup(): String = " -> $actionName $targetValue $valueName\n"
}

class Converters {

    @TypeConverter
    fun toTimePickerState(data: String?): SerializableTimePickerState? {
        if (data == null) {
            return null
        }
        return Json.decodeFromString(data)
    }

    @TypeConverter
    fun fromTimePickerState(someObjects: SerializableTimePickerState): String {
        return Json.encodeToString(someObjects)
    }

    @TypeConverter
    fun toCountableEntity(data: String?): CountableEntity? {
        if (data == null) {
            return null
        }
        return Json.decodeFromString(data)
    }

    @TypeConverter
    fun fromCountableEntity(someObjects: CountableEntity?): String {
        return Json.encodeToString(someObjects)
    }

    @TypeConverter
    fun toDayOfWeek(data: String?): Array<DayOfWeek> {
        if (data == null) {
            return arrayOf()
        }
        return Json.decodeFromString(data)
    }

    @TypeConverter
    fun fromDayOfWeek(someObjects: Array<DayOfWeek>): String {
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