package com.grigorevmp.habits.ui.HomeFragment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@Composable
fun HabitModule(habitViewModel: HabitViewModel? = null) {
    Surface(
        Modifier.fillMaxWidth()
    ) {
        Column {
            AddHabit(habitViewModel = habitViewModel)
            HabitList(habitViewModel)
        }
    }
}

@Composable
fun AddHabit(habitViewModel: HabitViewModel?) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(
        Modifier.fillMaxWidth()
    ) {
        TextField(modifier = Modifier.fillMaxWidth(),
            value = title,
            onValueChange = { newValue -> title = newValue },
            label = { Text("Title") })
        TextField(modifier = Modifier.fillMaxWidth(),
            value = description,
            onValueChange = { newValue -> description = newValue },
            label = { Text("Description") })

        Button(modifier = Modifier.align(Alignment.End),
            onClick = { habitViewModel?.addHabit(title, description) }) {
            Text("Add Habit")
        }
    }
}
