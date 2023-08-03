package com.grigorevmp.habits.ui.HomeFragment

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.grigorevmp.habits.data.HabitEntity

@Composable
fun HabitList(habitViewModel: HabitViewModel?) {
    val vm = habitViewModel ?: return

    val habits by vm.allHabits.collectAsState(initial = emptyList())
    var showingDialog by remember { mutableStateOf(false) }
    var selectedHabit by remember { mutableStateOf<HabitEntity?>(null) }

    LazyColumn(
        Modifier.fillMaxHeight()
    ) {
        items(habits) { habit ->
            Row(modifier = Modifier.clickable {
                selectedHabit = habit
                showingDialog = true
            }) {
                Checkbox(checked = habit.completed, onCheckedChange = { isChecked ->
                    vm.updateHabit(habit.copy(completed = isChecked))
                })
                Column {
                    Text(text = habit.title)
                    Text(text = habit.description)
                }
            }
        }
    }

    if (showingDialog) {
        selectedHabit?.let {
            EditHabitDialog(it, vm) {
                showingDialog = false
            }
        }
    }
}

@Preview
@Composable
fun PreviewHabit() {
    Row {
        Checkbox(checked = false, onCheckedChange = {})
        Column {
            Text(text = "habit.title")
            Text(text = "habit.description")

        }
    }
}


@Composable
fun EditHabitDialog(
    habit: HabitEntity,
    habitViewModel: HabitViewModel,
    onDismissRequest: () -> Unit
) {
    var title by remember { mutableStateOf(habit.title) }
    var description by remember { mutableStateOf(habit.description) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(onClick = {
                habitViewModel.updateHabit(habit.copy(title = title, description = description))
                onDismissRequest.invoke()
            }) {
                Text("Save")
            }
        },
        text = {
            Column {
                TextField(
                    value = title,
                    onValueChange = { newValue -> title = newValue },
                    label = { Text("Title") })
                TextField(
                    value = description,
                    onValueChange = { newValue -> description = newValue },
                    label = { Text("Description") })
            }
        }
    )
}