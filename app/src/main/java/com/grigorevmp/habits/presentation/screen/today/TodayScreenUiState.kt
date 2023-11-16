package com.grigorevmp.habits.presentation.screen.today

import com.grigorevmp.habits.presentation.screen.today.data.HabitStatisticItemUi
import com.grigorevmp.habits.presentation.screen.today.data.HabitWithDatesUI
import kotlinx.coroutines.flow.MutableStateFlow

data class TodayScreenUiState(
    var todayHabitData: MutableStateFlow<List<HabitWithDatesUI>> = MutableStateFlow(value = listOf()),
    var statisticData: MutableStateFlow<List<HabitStatisticItemUi>> = MutableStateFlow(value = listOf()),
)