package com.grigorevmp.habits.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.grigorevmp.habits.ui.habits.HabitListModule
import com.grigorevmp.habits.ui.home.HabitModule
import com.grigorevmp.habits.ui.home.HabitViewModel


@Composable
fun HomeScreen(viewModel: HabitViewModel? = null) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        HabitModule(viewModel)
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