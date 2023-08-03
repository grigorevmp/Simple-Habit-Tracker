package com.grigorevmp.habits.ui.main

import com.grigorevmp.habits.R


sealed class BottomNavItem(var title: String, var icon: Int, var screen_route: String) {

    data object Home : BottomNavItem("Home", R.drawable.ic_home, "home")
    data object HabitList : BottomNavItem("My Network", R.drawable.ic_task, "habit_list")
}