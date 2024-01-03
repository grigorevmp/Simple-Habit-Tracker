package com.grigorevmp.habits.presentation.screen.habits

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grigorevmp.habits.R
import com.grigorevmp.habits.data.CountableEntity
import com.grigorevmp.habits.data.HabitCategory
import com.grigorevmp.habits.data.HabitEntity
import com.grigorevmp.habits.data.SerializableTimePickerState
import com.grigorevmp.habits.presentation.screen.habits.data.StatYear
import com.grigorevmp.habits.presentation.screen.habits.elements.AllHabitList
import com.grigorevmp.habits.presentation.screen.habits.elements.bottom_sheet.AddEditBottomSheet
import java.time.DayOfWeek


@Composable
fun HabitListScreen(habitsViewModel: HabitsViewModel) {
    val habitsData by habitsViewModel.uiState.habitsData.collectAsState()
    val statisticData by habitsViewModel.uiState.statisticData.collectAsState()

    HabitListScreen(
        habitsData = habitsData,
        statisticData = statisticData,
        updateHabitEntity = { context: Context, habitEntity: HabitEntity ->
            habitsViewModel.updateHabit(context, habitEntity)
        },
        deleteHabitEntity = { habitEntity: HabitEntity ->
            habitsViewModel.deleteHabit(habitEntity)
        },
        prepareStat = { payload: () -> Unit ->
            habitsViewModel.loadData {
                payload()
            }
        },
    ) { context: Context,
        title: String,
        description: String,
        daysForHabit: Array<DayOfWeek>,
        useAlert: Boolean,
        timeState: SerializableTimePickerState,
        countable: Boolean,
        countableEntity: CountableEntity?,
        habitCategory: HabitCategory ->
        habitsViewModel.addHabit(
            context,
            title,
            description,
            daysForHabit,
            useAlert,
            SerializableTimePickerState(
                hour = timeState.hour,
                minute = timeState.minute,
            ),
            countable,
            countableEntity,
            habitCategory
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitListScreen(
    habitsData: List<HabitEntity>,
    statisticData: Map<Long, List<StatYear>>,
    updateHabitEntity: (Context, HabitEntity) -> Unit,
    deleteHabitEntity: (HabitEntity) -> Unit,
    prepareStat: (() -> Unit) -> Unit,
    addHabitEntity: (Context, String, String, Array<DayOfWeek>, Boolean, SerializableTimePickerState, Boolean, CountableEntity?, HabitCategory) -> Unit,
) {

    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var allHabitStatData by remember { mutableStateOf<Map<Long, List<StatYear>>>(mapOf()) }

    var dataIsReady by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        prepareStat {
            dataIsReady = true
        }
    }

    if (dataIsReady) {
        allHabitStatData = statisticData
    }

    AddEditBottomSheet(
        openBottomSheet,
        bottomSheetState,
        addHabitEntity = addHabitEntity
    ) { state ->
        openBottomSheet = state
    }

    AnimatedVisibility(
        visible = dataIsReady,
        enter = fadeIn(
            animationSpec = TweenSpec(500, 0, FastOutSlowInEasing)
        )
    ) {
        Surface(Modifier.fillMaxSize()) {
            Column {
                AllHabitList(
                    allHabits = habitsData,
                    updateHabitEntity = updateHabitEntity,
                    deleteHabitEntity = deleteHabitEntity,
                    getAllHabitDates = allHabitStatData,
                )
            }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomEnd
            ) {
                FloatingActionButton(
                    onClick = { openBottomSheet = true },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = stringResource(R.string.habit_screen_localize_icon_description)
                    )
                }
            }
        }
    }

    AnimatedVisibility(
        visible = !dataIsReady,
        exit = fadeOut(
            animationSpec = TweenSpec(500, 0, FastOutLinearInEasing)
        )
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp, bottom = 12.dp),
            text = stringResource(R.string.habit_screen_your_habits_title),
            fontSize = 24.sp
        )
    }
}


@Preview(showBackground = true)
@Composable
fun HabitListScreenPreview() {
    HabitListScreen(
        habitsData = listOf(
            HabitEntity(
                id = 0,
                title = "Title",
                description = "Description",
                days = arrayOf(DayOfWeek.FRIDAY),
                alertEnabled = true,
                time = SerializableTimePickerState(10, 10),
                completed = false,
            )
        ),
        statisticData = mapOf(),
        updateHabitEntity = { _, _ -> },
        deleteHabitEntity = { _ -> },
        prepareStat = { _ -> },
        addHabitEntity = { _, _, _, _, _, _, _, _, _ -> },
    )
}

