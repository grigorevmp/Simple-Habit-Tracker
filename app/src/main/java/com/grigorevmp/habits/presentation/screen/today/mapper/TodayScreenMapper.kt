package com.grigorevmp.habits.presentation.screen.today.mapper

import android.content.Context
import java.time.DayOfWeek
import java.time.Month
import java.time.format.TextStyle
import javax.inject.Inject

class TodayScreenMapper @Inject constructor() {

    fun getFancyTodayString(
        context: Context,
        dayOfMonth: Int,
        month: Month,
        dayOfWeek: DayOfWeek,
    ): String {
        return  "$dayOfMonth ${month.toUniversalMonth(context)} · ${dayOfWeek.toUniversalMonth(context)}"
    }

    private fun Month.toUniversalMonth(context: Context): String? {
        val locale = context.resources.configuration.locales.get(0)
        return this.getDisplayName(TextStyle.FULL, locale)
    }

    private fun DayOfWeek.toUniversalMonth(context: Context): String? {
        val locale = context.resources.configuration.locales.get(0)
        return this.getDisplayName(TextStyle.FULL, locale)
    }

}