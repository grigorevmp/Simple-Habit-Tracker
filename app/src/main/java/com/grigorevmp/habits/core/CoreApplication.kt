package com.grigorevmp.habits.core

import android.app.Application
import com.grigorevmp.habits.data.HabitDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CoreApplication: Application() {

    override fun onCreate() {
        super.onCreate()
    }
}