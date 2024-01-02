package com.grigorevmp.habits.data.repository

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.grigorevmp.habits.core.utils.Changelogs
import com.grigorevmp.habits.presentation.theme.ThemePreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import java.time.LocalDate
import javax.inject.Inject


class PreferencesRepository @Inject constructor(
    val context: Application
) {

    private val preferences = context.getSharedPreferences(MAIN_PREFERENCES, Context.MODE_PRIVATE)

    fun getAppThemeFlow()= preferences.observeKey<String>(APP_THEME, ThemePreference.System.name)

    fun getAppTheme(): ThemePreference {
        val themeString = preferences.getString(APP_THEME, ThemePreference.System.name) ?: ThemePreference.System.name

        return ThemePreference.valueOf(themeString)
    }

    fun setAppTheme(theme: ThemePreference) {
        preferences.edit()
            .putString(APP_THEME, theme.name)
            .apply()
    }

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

    fun getVersion() = preferences.getLong(VERSION_CODE, Changelogs.version.toLong())

    fun setVersion(version: Long) {
        preferences.edit()
            .putLong(VERSION_CODE, version)
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

    private inline fun <reified T> SharedPreferences.observeKey(key: String, default: T?): Flow<T?> {
        val flow = MutableStateFlow(getItem(key, default))

        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, k ->
            if (key == k) {
                try {
                    flow.value = getItem(key, default)!!
                } catch (e: Exception) {
                    flow.value = null
                }
            }
        }

        return flow
            .onCompletion { unregisterOnSharedPreferenceChangeListener(listener) }
            .onStart { registerOnSharedPreferenceChangeListener(listener) }
    }

    private inline fun <reified T> SharedPreferences.getItem(key: String, default: T?): T? {
        @Suppress("UNCHECKED_CAST")
        return when (default) {
            is String? -> getString(key, default) as T?
            is Int -> getInt(key, default) as T
            is Long -> getLong(key, default) as T
            is Boolean -> getBoolean(key, default) as T
            is Float -> getFloat(key, default) as T
            is Set<*> -> getStringSet(key, default as Set<String>) as T
            else -> throw IllegalArgumentException("generic type not handle ${T::class.java.name}")
        }
    }


    companion object {
        const val MAIN_PREFERENCES = "main_habits_prefs"

        const val LAST_SYNC = "last_sync"
        const val FIRST_PERMISSION_REQUEST = "show_first_permission_request_14_11_23"
        const val LONGER_DAY = "longer_day"
        const val APP_THEME = "app_theme"
        const val VERSION_CODE = "version_code"

        const val CONGRATS_EMOJI = "congratulation_emoji"
        val DEFAULT_EMOJI = setOf("⚡", "🫰", "🩶", "🤍", "🤎", "💛", "🧡", "💖", "❤️", "🩵", "💜", "💙", "💚", "❤️‍🔥", "🔥", "🧨", "✨", "🎉", "🎊")
    }
}