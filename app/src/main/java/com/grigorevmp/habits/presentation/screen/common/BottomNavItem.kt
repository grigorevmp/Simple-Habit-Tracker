package com.grigorevmp.habits.presentation.screen.common

import com.grigorevmp.habits.R


sealed class BottomNavItem(var title: String, var icon: Int, var screenRoute: String) {

    data object Today : BottomNavItem("Home", R.drawable.ic_home, "today")
    data object Habits : BottomNavItem("Habits", R.drawable.ic_task, "habit_list")
    data object Settings : BottomNavItem("Settings", R.drawable.ic_settings, "settings")
}