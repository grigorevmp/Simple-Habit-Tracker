package com.grigorevmp.habits.di

import android.app.Application
import androidx.room.Room
import com.grigorevmp.habits.data.database.HabitDatabase
import com.grigorevmp.habits.data.HabitWithDateDao
import com.grigorevmp.habits.data.date.DateDao
import com.grigorevmp.habits.data.database.migration.MIGRATION_1_2
import com.grigorevmp.habits.data.database.migration.MIGRATION_2_3
import com.grigorevmp.habits.data.database.migration.MIGRATION_3_4
import com.grigorevmp.habits.data.habit.HabitRefDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application, callback: HabitDatabase.Callback): HabitDatabase {
        return Room.databaseBuilder(application, HabitDatabase::class.java, "habit_database")
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .addMigrations(MIGRATION_1_2)
            .addMigrations(MIGRATION_2_3)
            .addMigrations(MIGRATION_3_4)
//        .setQueryCallback({ sqlQuery, bindArgs ->
//            Log.d("DB", "SQL Query: $sqlQuery SQL Args: $bindArgs")
//        }, Executors.newSingleThreadExecutor())
            .build()
    }

    @Provides
    fun provideHabitDao(db: HabitDatabase): HabitWithDateDao {
        return db.getHabitDao()
    }

    @Provides
    fun provideDateDao(db: HabitDatabase): DateDao {
        return db.getDateDao()
    }

    @Provides
    fun provideHabitRefDao(db: HabitDatabase): HabitRefDao {
        return db.getHabitRefDao()
    }
}