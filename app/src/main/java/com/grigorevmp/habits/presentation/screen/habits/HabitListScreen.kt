package com.grigorevmp.habits.presentation.screen.habits

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.grigorevmp.habits.R
import com.grigorevmp.habits.data.CountableEntity
import com.grigorevmp.habits.data.HabitEntity
import com.grigorevmp.habits.data.SerializableTimePickerState
import com.grigorevmp.habits.presentation.screen.habits.elements.AllHabitList
import com.grigorevmp.habits.presentation.screen.habits.elements.dialogs.AddHabitDialog
import java.time.DayOfWeek


@Composable
fun HabitListScreen(habitsViewModel: HabitsViewModel) {
    val allHabits by habitsViewModel.habits.collectAsState()

    HabitListScreen(
        allHabits = allHabits,
        updateHabitEntity = { context: Context, habitEntity: HabitEntity ->
            habitsViewModel.updateHabit(context, habitEntity)
        },
        deleteHabitEntity = { habitEntity: HabitEntity ->
            habitsViewModel.deleteHabit(habitEntity)
        }
    ) { context: Context,
        title: String,
        description: String,
        daysForHabit: Array<DayOfWeek>,
        useAlert: Boolean,
        timeState: SerializableTimePickerState,
        countable: Boolean,
        countableEntity: CountableEntity? ->
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
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitListScreen(
    allHabits: List<HabitEntity>,
    updateHabitEntity: (Context, HabitEntity) -> Unit,
    deleteHabitEntity: (HabitEntity) -> Unit,
    addHabitEntity: (Context, String, String, Array<DayOfWeek>, Boolean, SerializableTimePickerState, Boolean, CountableEntity?) -> Unit,
) {
    var dialogShown by remember { mutableStateOf(false) }

    if (dialogShown) {
        Dialog(
            onDismissRequest = { dialogShown = false }
        ) {
            Surface(
                modifier = Modifier.clip(RoundedCornerShape(12.dp)),
                color = MaterialTheme.colorScheme.surfaceContainerHighest
            ) {
                AddHabitDialog(addHabitEntity) {
                    dialogShown = false
                }
            }
        }
    }

    Surface(Modifier.fillMaxSize()) {
        Column {
            AllHabitList(
                allHabits = allHabits,
                updateHabitEntity = updateHabitEntity,
                deleteHabitEntity = deleteHabitEntity
            )
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
        ) {
            FloatingActionButton(
                onClick = { dialogShown = true },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.habit_screen_localize_icon_description))
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HabitListScreenPreview() {
    HabitListScreen(
        allHabits = listOf(
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
        updateHabitEntity = { _, _ -> },
        deleteHabitEntity = { _ -> },
        addHabitEntity = { _, _, _, _, _, _, _, _ -> },
    )
}

