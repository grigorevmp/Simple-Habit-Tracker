package com.grigorevmp.habits.di

import android.app.Application
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import com.grigorevmp.habits.data.HabitDatabase
import com.grigorevmp.habits.data.HabitWithDateDao
import com.grigorevmp.habits.data.data.DateDao
import com.grigorevmp.habits.data.habit.HabitRefDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.Executors
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