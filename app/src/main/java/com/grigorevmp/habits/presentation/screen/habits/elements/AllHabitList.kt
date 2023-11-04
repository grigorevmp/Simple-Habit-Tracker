package com.grigorevmp.habits.presentation.screen.habits.elements

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grigorevmp.habits.data.HabitEntity
import com.grigorevmp.habits.data.SerializableTimePickerState
import com.grigorevmp.habits.presentation.screen.habits.elements.dialogs.EditHabitDialog
import java.time.DayOfWeek
import java.time.LocalDate

@Composable
fun AllHabitList(
    allHabits: List<HabitEntity>,
    updateHabitEntity: (Context, HabitEntity) -> Unit,
    deleteHabitEntity: (HabitEntity) -> Unit,
) {
    HabitList(
        allHabits = allHabits,
        updateHabitEntity = updateHabitEntity,
        deleteHabitEntity = deleteHabitEntity,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HabitList(
    allHabits: List<HabitEntity>,
    updateHabitEntity: (Context, HabitEntity) -> Unit,
    deleteHabitEntity: (HabitEntity) -> Unit,
) {
    val listState = rememberLazyListState()

    LazyColumn(
        Modifier.fillMaxSize(),
        state = listState,
    ) {
        item {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "Your habits",
                fontSize = 24.sp
            )
        }

        synchronized(allHabits) {
            items(
                items = allHabits,
                key = { habit -> habit.id }
            ) { item ->
                if (!item.deleted) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItemPlacement()
                    ) {
                        HabitCard(
                            habitEntity = item,
                            updateHabitEntity = updateHabitEntity,
                            deleteHabitEntity = deleteHabitEntity,
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewHabit() {
    HabitCard(
        habitEntity = HabitEntity(
            title = "Заголовок",
            description = "Описание",
            alertEnabled = true,
            days = arrayOf(LocalDate.now().dayOfWeek),
            time = SerializableTimePickerState(11, 12),
        ),
        updateHabitEntity = { _, _ -> },
        deleteHabitEntity = { _ -> },
    )
}


@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
fun EditHabitPreview() {
    EditHabitDialog(
        HabitEntity(
            id = 0,
            title = "Title",
            description = "Description",
            days = arrayOf(DayOfWeek.FRIDAY),
            alertEnabled = true,
            time = SerializableTimePickerState(10, 10),
            completed = false,
        ),
        updateHabitEntity = { _, _ -> },
        deleteHabitEntity = { _ -> },
        { },
    )
}