package com.grigorevmp.habits.presentation.screen.today

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grigorevmp.habits.R
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
    val allHabitsStatisticData by habitViewModel.uiState.statisticData.collectAsState()
    val allHabitsWithDateData by habitViewModel.uiState.todayHabitData.collectAsState()

    LaunchedEffect(Unit) {
        habitViewModel.init(context, daysCount = 10)
    }

    TodayScreen(
        prepareHabitsList = { payload: () -> Unit ->
            habitViewModel.getPreparedHabitsList(context, daysCount = 10) {
                payload()
            }
            habitViewModel.updateWeekStatistic()
        },
        allHabitsWithDateData = allHabitsWithDateData,
        allHabitsStatisticData = allHabitsStatisticData,
        updateHabitRef = { dateId: Long, id: Long, habitType: HabitType ->
            habitViewModel.updateHabitRef(dateId, id, habitType)
        },
        updateHabitRefCountable = { dateId: Long, id: Long, habitType: HabitType, value: Int ->
            habitViewModel.updateHabitRefCountable(dateId, id, habitType, value)
        },
        getRandomEmoji = { seed: Long ->
            habitViewModel.getRandomEmoji(seed)
        }
    )
}

@Composable
fun TodayScreen(
    prepareHabitsList: (() -> Unit) -> Unit,
    allHabitsWithDateData: List<HabitWithDatesUI>,
    allHabitsStatisticData: List<HabitStatisticItemUi>,
    updateHabitRef: (Long, Long, HabitType) -> Unit,
    updateHabitRefCountable: (Long, Long, HabitType, Int) -> Unit,
    getRandomEmoji: (Long) -> String,
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
    }


    Surface(Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = dataIsReady,
            enter = fadeIn(
                animationSpec = TweenSpec(500, 0, FastOutSlowInEasing)
            )
        ) {
            TodayView(
                allHabitsWithDate,
                allHabitsStatistic,
                updateHabitRef,
                updateHabitRefCountable,
                getRandomEmoji,
            )
        }

        AnimatedVisibility(
            visible = !dataIsReady,
            exit = fadeOut(
                animationSpec = TweenSpec(500, 0, FastOutLinearInEasing)
            )
        ) {
            TodayShimmedView()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TodayView(
    allHabits: List<HabitWithDatesUI>,
    allHabitsStatisticData: List<HabitStatisticItemUi>,
    updateHabitRef: (Long, Long, HabitType) -> Unit,
    updateHabitRefCountable: (Long, Long, HabitType, Int) -> Unit,
    getRandomEmoji: (Long) -> String,
) {
    val listState = rememberLazyListState()
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItemPlacement(),
                    shape = RoundedCornerShape(0.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                ) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = stringResource(R.string.today_screen_statistics_title),
                        fontSize = 24.sp
                    )
                }
            }

            item {
                DaysStatisticCard(allHabitsStatisticData, getRandomEmoji = getRandomEmoji)
            }

            stickyHeader {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItemPlacement(),
                    shape = RoundedCornerShape(0.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                ) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = stringResource(R.string.today_screen_latest_title),
                        fontSize = 24.sp
                    )
                }
            }

            itemsIndexed(allHabits) { _, habitsForDate ->

                HabitsForDayCard(
                    habitsForDate,
                    updateHabitRef,
                    updateHabitRefCountable,
                )
            }
        }
    }
}

@Composable
fun TodayShimmedView() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.fillMaxSize()
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(0.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
        ) {
            Text(
                modifier = Modifier
                    .padding(16.dp),
                text = stringResource(id = R.string.today_screen_statistics_title),
                fontSize = 24.sp
            )
        }

        DaysStatisticCard(
            allHabitsStatisticData = listOf(
                HabitStatisticItemUi(0, 0, 1, 1L),
                HabitStatisticItemUi(0, 0, 1, 1L),
                HabitStatisticItemUi(0, 0, 1, 1L),
                HabitStatisticItemUi(0, 0, 1, 1L),
                HabitStatisticItemUi(0, 0, 1, 1L),
                HabitStatisticItemUi(0, 0, 1, 1L),
                HabitStatisticItemUi(0, 0, 1, 1L),
            ),
            modifier = Modifier.shimmer(),
        ) { "" }
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
        updateHabitRefCountable = { _, _, _, _ -> },
        getRandomEmoji = { _ -> ""}
    )
}


@Preview(showBackground = true)
@Composable
fun TodayShimmedViewPreview() {
    TodayShimmedView()
}