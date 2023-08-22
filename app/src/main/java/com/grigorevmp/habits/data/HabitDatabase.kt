package com.grigorevmp.habits.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.grigorevmp.habits.data.data.DateDao
import com.grigorevmp.habits.data.data.DateEntity
import com.grigorevmp.habits.data.habit.HabitRefDao
import com.grigorevmp.habits.data.habit.HabitRefEntity
import com.grigorevmp.habits.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Provider


@Database(
    entities = [
        HabitEntity::class,
        DateEntity::class,
        HabitRefEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class HabitDatabase : RoomDatabase() {
    abstract fun getHabitDao(): HabitWithDateDao

    abstract fun getDateDao(): DateDao

    abstract fun getHabitRefDao(): HabitRefDao


    class Callback @Inject constructor(
        private val database: Provider<HabitDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback()

}