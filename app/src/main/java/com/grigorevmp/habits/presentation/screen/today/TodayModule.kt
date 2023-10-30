package com.grigorevmp.habits.presentation.screen.today

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grigorevmp.habits.data.habit.HabitType
import com.grigorevmp.habits.presentation.screen.today.data.HabitEntityUI
import com.grigorevmp.habits.presentation.screen.today.data.HabitWithDatesUI
import com.grigorevmp.habits.presentation.screen.today.elements.HabitsForDayCard


@Composable
fun TodayModule(habitViewModel: HabitViewModel? = null) {
    var allHabitsWithDateData by remember { mutableStateOf<List<HabitWithDatesUI>>(mutableListOf()) }
    var dataIsReady by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        habitViewModel?.getPreparedHabitsList(daysCount = 10) {
            dataIsReady = true
        }
    }

    if (dataIsReady) {
        allHabitsWithDateData = habitViewModel!!.uiState.value

        TodayView(
            allHabitsWithDateData,
            habitViewModel
        )
    }
}

@Composable
fun TodayView(
    allHabits: List<HabitWithDatesUI>,
    habitViewModel: HabitViewModel?
) {
    val listState = rememberLazyListState()

    Surface(Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState,
            ) {
                itemsIndexed(allHabits) { _, habitsForDate ->
                    HabitsForDayCard(
                        habitsForDate,
                        habitViewModel
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TodayModulePreview() {
    TodayView(
        allHabits = listOf(
            HabitWithDatesUI(
                habits = mutableListOf(
                    HabitEntityUI(
                        id = 0,
                        dateId = 3,
                        title = "Title",
                        description = "Description",
                        type = HabitType.Missed,
                    )
                ),
                date = "Current date"
            )
        ),
        habitViewModel = null
    )
}