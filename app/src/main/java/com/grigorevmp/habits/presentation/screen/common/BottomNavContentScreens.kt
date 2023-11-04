package com.grigorevmp.habits.presentation.screen.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.grigorevmp.habits.presentation.screen.habits.HabitListScreen
import com.grigorevmp.habits.presentation.screen.habits.HabitsViewModel
import com.grigorevmp.habits.presentation.screen.settings.SettingsScreen
import com.grigorevmp.habits.presentation.screen.settings.SettingsScreenViewModel
import com.grigorevmp.habits.presentation.screen.today.TodayScreenViewModel
import com.grigorevmp.habits.presentation.screen.today.TodayScreen


@Composable
fun TodayNavScreen(viewModel: TodayScreenViewModel) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TodayScreen(viewModel)
    }
}

@Composable
fun HabitListNavScreen(habitsViewModel: HabitsViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        HabitListScreen(habitsViewModel)
    }
}


@Composable
fun SettingsNavScreen(viewModel: SettingsScreenViewModel) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SettingsScreen(viewModel)
    }
}