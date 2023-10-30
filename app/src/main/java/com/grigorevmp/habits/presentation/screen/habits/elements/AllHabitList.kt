package com.grigorevmp.habits.presentation.screen.habits.elements

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.grigorevmp.habits.data.HabitEntity
import com.grigorevmp.habits.data.SerializableTimePickerState
import com.grigorevmp.habits.presentation.screen.habits.elements.dialogs.EditHabitDialog
import com.grigorevmp.habits.presentation.screen.today.HabitViewModel
import java.time.DayOfWeek
import java.time.LocalDate

@Composable
fun AllHabitList(habitViewModel: HabitViewModel?) {
    val vm = habitViewModel ?: return
    val allHabits by vm.habits.collectAsState()

    HabitList(habitViewModel, allHabits)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HabitList(vm: HabitViewModel?, allHabits: List<HabitEntity>) {

    val listState = rememberLazyListState()

    LazyColumn(
        Modifier.fillMaxSize(),
        state = listState,
    ) {
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
                        HabitCard(vm, item)
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
        null,
        HabitEntity(
            title = "Заголовок",
            description = "Описание",
            alertEnabled = true,
            days = arrayOf(LocalDate.now().dayOfWeek),
            time = SerializableTimePickerState(11, 12),
        )
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
        null,
        { },
        { },
    )
}