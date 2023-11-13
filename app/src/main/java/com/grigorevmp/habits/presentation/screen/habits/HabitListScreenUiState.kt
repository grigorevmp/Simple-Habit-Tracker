package com.grigorevmp.habits.presentation.screen.habits

import com.grigorevmp.habits.data.HabitEntity
import com.grigorevmp.habits.presentation.screen.habits.data.StatYear
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class HabitListScreenUiState(
    var habitsData: StateFlow<List<HabitEntity>>,
    var statisticData: MutableStateFlow<Map<Long, List<StatYear>>> = MutableStateFlow(mapOf()),
)