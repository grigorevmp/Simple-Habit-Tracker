package com.grigorevmp.habits.di

import android.app.Application
import androidx.room.Room
import com.grigorevmp.habits.data.HabitDao
import com.grigorevmp.habits.data.HabitDatabase
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
            .build()
    }

    @Provides
    fun provideArticleDao(db: HabitDatabase): HabitDao {
        return db.getHabitDao()
    }
}