package com.grigorevmp.habits.ui.main

import com.grigorevmp.habits.R


sealed class BottomNavItem(var title: String, var icon: Int, var screenRoute: String) {

    data object Home : BottomNavItem("Home", R.drawable.ic_home, "home")
    data object Habits : BottomNavItem("Habits", R.drawable.ic_task, "habit_list")
}