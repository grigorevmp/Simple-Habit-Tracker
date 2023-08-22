package com.grigorevmp.habits.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import java.util.Locale


@Composable
fun HabitModule(habitViewModel: HabitViewModel? = null) {

    fun getDatesForTheNextDays(days: Int): List<Long> {
        val dates = mutableListOf<Long>()
        val calendar = Calendar.getInstance()
        for (i in 0 until days) {
            dates.add(calendar.timeInMillis)
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        return dates
    }

    val dates = remember { getDatesForTheNextDays(days = 10) } // get the next 10 days
    var isDialogOpen by remember { mutableStateOf(false) }
    val habits by habitViewModel!!.allHabits.collectAsState(initial = emptyList())

    Surface(Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn {
                items(dates) { date ->
                    val currentDate by habitViewModel!!.getDate(LocalDate.now()).collectAsState(initial = null)

                    // place any custom view here just like this Text composable
                    Text(
                        text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(date)), // format date as you need
                        modifier = Modifier.padding(8.dp)
                    )

                    for (habit in habits) {
                        val forDate =
                            currentDate?.id?.let { habitViewModel!!.getHabitWithDate(it, habit.habit.id).collectAsState(initial = null) }
                        if (forDate != null) {
                            Text(
                                text = habit.habit.title + "DONE",
                                modifier = Modifier.padding(4.dp)
                            )
                        } else {
                            Text(
                                text = habit.habit.title,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                }
            }
        }
    }

//    Surface(
//        Modifier.fillMaxWidth()
//    ) {
//        Column {
//            // AddHabit(habitViewModel = habitViewModel)
//            //HabitList(habitViewModel)
//        }
//    }
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

@Preview(showBackground = true)
@Composable
fun HomeModulePreview() {
    HabitModule()
}


