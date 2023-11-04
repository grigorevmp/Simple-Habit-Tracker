package com.grigorevmp.habits.presentation.screen.common

import com.grigorevmp.habits.R


sealed class BottomNavItem(var title: String, var icon: Int, var screenRoute: String) {

    data class Today(val tabName: String) : BottomNavItem(tabName, R.drawable.ic_home, "today")

    data class Habits(val tabName: String) : BottomNavItem(tabName, R.drawable.ic_task, "habit_list")

    data class Settings(val tabName: String) : BottomNavItem(tabName, R.drawable.ic_settings, "settings")
}