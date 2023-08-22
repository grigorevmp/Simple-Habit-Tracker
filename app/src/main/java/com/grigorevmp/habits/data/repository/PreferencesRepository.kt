package com.grigorevmp.habits.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.serialization.json.Json
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.serialization.encodeToString


class PreferencesRepository @Inject constructor(
    @ActivityContext val context: Context
) {

    private val preferences = context.getSharedPreferences(MAIN_PREFERENCES, Context.MODE_PRIVATE)


    fun getLastSyncDate(): LocalDate {
        val lastSyncString = preferences.getString(LAST_SYNC, "")

        return if (lastSyncString.isNullOrBlank()) {
            LocalDate.now()
        } else {
            Json.decodeFromString(lastSyncString)
        }
    }


    fun updateLastSyncDate() {
        val current = LocalDate.now()
        val dateString = Json.encodeToString(current)

        preferences.edit()
            .putString(LAST_SYNC, dateString)
            .apply()
    }


    companion object {
        const val MAIN_PREFERENCES = "main_habits_prefs"

        const val LAST_SYNC = "last_sync"
    }
}