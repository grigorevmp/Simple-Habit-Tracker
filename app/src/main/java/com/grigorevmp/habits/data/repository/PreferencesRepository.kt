package com.grigorevmp.habits.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ActivityContext
import java.time.LocalDate
import javax.inject.Inject


class PreferencesRepository @Inject constructor(
    @ActivityContext val context: Context
) {

    private val preferences = context.getSharedPreferences(MAIN_PREFERENCES, Context.MODE_PRIVATE)


    fun getLastSyncDate(): LocalDate {
        val lastSyncString = preferences.getLong(LAST_SYNC, 0)

        return if (lastSyncString == 0L) {
            LocalDate.now()
        } else {
            LocalDate.ofEpochDay(lastSyncString)
        }
    }


    fun updateLastSyncDate() {
        val current = LocalDate.now()

        preferences.edit()
            .putLong(LAST_SYNC, current.toEpochDay())
            .apply()
    }


    companion object {
        const val MAIN_PREFERENCES = "main_habits_prefs"

        const val LAST_SYNC = "last_sync"
    }
}