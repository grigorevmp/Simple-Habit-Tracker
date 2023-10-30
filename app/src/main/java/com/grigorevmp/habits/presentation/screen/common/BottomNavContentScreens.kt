package com.grigorevmp.habits.presentation.screen.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.grigorevmp.habits.presentation.screen.habits.HabitListModule
import com.grigorevmp.habits.presentation.screen.settings.SettingsModule
import com.grigorevmp.habits.presentation.screen.today.HabitViewModel
import com.grigorevmp.habits.presentation.screen.today.TodayModule


@Composable
fun TodayScreen(viewModel: HabitViewModel? = null) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TodayModule(viewModel)
    }
}

@Composable
fun HabitListScreen(viewModel: HabitViewModel? = null) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        HabitListModule(viewModel)
    }
}


@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SettingsModule()
    }
}