package com.grigorevmp.habits.presentation.screen.today

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grigorevmp.habits.data.habit.HabitType
import com.grigorevmp.habits.presentation.screen.today.data.HabitEntityUI
import com.grigorevmp.habits.presentation.screen.today.data.HabitStatisticItemUi
import com.grigorevmp.habits.presentation.screen.today.data.HabitWithDatesUI
import com.grigorevmp.habits.presentation.screen.today.elements.DaysStatisticCard
import com.grigorevmp.habits.presentation.screen.today.elements.HabitsForDayCard
import com.valentinilk.shimmer.shimmer


@Composable
fun TodayScreen(habitViewModel: TodayScreenViewModel) {
    val context = LocalContext.current
    val allHabitsStatisticData by habitViewModel.statisticUiState.collectAsState()

    TodayScreen(
        prepareHabitsList = { payload: () -> Unit ->
            habitViewModel.getPreparedHabitsList(context, daysCount = 10) {
                payload()
            }
            habitViewModel.updateWeekStatistic()
        },
        allHabitsWithDateData = habitViewModel.uiState.value,
        allHabitsStatisticData = allHabitsStatisticData,
        updateHabitRef = { dateId: Long, id: Long, habitType: HabitType ->
            habitViewModel.updateHabitRef(dateId, id, habitType)
        },
    )
}

@Composable
fun TodayScreen(
    prepareHabitsList: (() -> Unit) -> Unit,
    allHabitsWithDateData: List<HabitWithDatesUI>,
    allHabitsStatisticData: List<HabitStatisticItemUi>,
    updateHabitRef: (Long, Long, HabitType) -> Unit,
) {
    var allHabitsWithDate by remember { mutableStateOf<List<HabitWithDatesUI>>(mutableListOf()) }
    var allHabitsStatistic by remember { mutableStateOf<List<HabitStatisticItemUi>>(mutableListOf()) }
    var dataIsReady by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        prepareHabitsList {
            dataIsReady = true
        }
    }

    if (dataIsReady) {
        allHabitsWithDate = allHabitsWithDateData
        allHabitsStatistic = allHabitsStatisticData

        TodayView(
            allHabitsWithDate,
            allHabitsStatistic,
            updateHabitRef
        )
    } else {
        TodayShimmedView()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TodayView(
    allHabits: List<HabitWithDatesUI>,
    allHabitsStatisticData: List<HabitStatisticItemUi>,
    updateHabitRef: (Long, Long, HabitType) -> Unit,
) {
    val listState = rememberLazyListState()

    Surface(Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .animateContentSize(),
                state = listState,
            ) {
                stickyHeader {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(0.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                    ) {
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = "Statistics",
                            fontSize = 24.sp
                        )
                    }
                }

                item {
                    DaysStatisticCard(allHabitsStatisticData)
                }

                stickyHeader {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(0.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                    ) {
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = "Latest habits",
                            fontSize = 24.sp
                        )
                    }
                }

                itemsIndexed(allHabits) { _, habitsForDate ->

                    HabitsForDayCard(
                        habitsForDate,
                        updateHabitRef
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TodayShimmedView() {
    Surface(Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .animateContentSize(),
            ) {
                stickyHeader {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(0.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                    ) {
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = "Statistics",
                            fontSize = 24.sp
                        )
                    }
                }

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(256.dp)
                            .padding(horizontal = 8.dp, vertical = 8.dp)
                            .shimmer(),
                    ) { }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TodayScreenPreview() {
    TodayScreen(
        prepareHabitsList = { },
        allHabitsWithDateData = listOf(
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
        allHabitsStatisticData = listOf(),
        updateHabitRef = { _, _, _ -> },
    )
}


@Preview(showBackground = true)
@Composable
fun TodayShimmedViewPreview() {
    TodayShimmedView()
}