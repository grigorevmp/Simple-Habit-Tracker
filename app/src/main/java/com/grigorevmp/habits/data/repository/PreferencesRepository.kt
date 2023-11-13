package com.grigorevmp.habits.data.repository

import android.app.Application
import android.content.Context
import java.time.LocalDate
import javax.inject.Inject


class PreferencesRepository @Inject constructor(
    val context: Application
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

    fun getPermissionShown() = preferences.getBoolean(FIRST_PERMISSION_REQUEST, false)

    fun setPermissionShown() {
        preferences.edit()
            .putBoolean(FIRST_PERMISSION_REQUEST, true)
            .apply()
    }

    fun getCongratsEmoji() = preferences.getStringSet(CONGRATS_EMOJI, DEFAULT_EMOJI) ?: DEFAULT_EMOJI

    fun setCongratsEmoji(emoji: Set<String>) {
        preferences.edit()
            .putStringSet(CONGRATS_EMOJI, emoji)
            .apply()
    }

    fun getLongerDateFlag() = preferences.getBoolean(LONGER_DAY, false)

    fun setLongerDateFlag(makeDayLonger: Boolean) {
        preferences.edit()
            .putBoolean(LONGER_DAY, makeDayLonger)
            .apply()
    }


    companion object {
        const val MAIN_PREFERENCES = "main_habits_prefs"

        const val LAST_SYNC = "last_sync"
        const val FIRST_PERMISSION_REQUEST = "show_first_permission_request_14_11_23"
        const val LONGER_DAY = "longer_day"

        const val CONGRATS_EMOJI = "congratulation_emoji"
        val DEFAULT_EMOJI = setOf("⚡", "🫰", "🩶", "🤍", "🤎", "💛", "🧡", "💖", "❤️", "🩵", "💜", "💙", "💚", "❤️‍🔥", "🔥", "🧨", "✨", "🎉", "🎊")
    }
}