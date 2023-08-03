package com.grigorevmp.habits.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.grigorevmp.habits.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Provider


@Database(
    entities = [HabitEntity::class], version = 1, exportSchema = false
)
abstract class HabitDatabase : RoomDatabase() {
    abstract fun getHabitDao(): HabitDao


    class Callback @Inject constructor(
        private val database: Provider<HabitDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback()

}