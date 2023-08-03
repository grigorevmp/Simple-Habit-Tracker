package com.grigorevmp.habits.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.grigorevmp.habits.ui.HomeFragment.HabitModule
import com.grigorevmp.habits.ui.HomeFragment.HabitViewModel


@Composable
fun HomeScreen(viewModel: HabitViewModel? = null) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        HabitModule(viewModel)
    }
}

@Composable
fun HabitListScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = "Habit list",
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
    }
}