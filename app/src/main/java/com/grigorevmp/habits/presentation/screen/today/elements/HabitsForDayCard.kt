package com.grigorevmp.habits.presentation.screen.today.elements

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grigorevmp.habits.R
import com.grigorevmp.habits.data.habit.HabitType
import com.grigorevmp.habits.presentation.screen.today.HabitViewModel
import com.grigorevmp.habits.presentation.screen.today.data.HabitEntityUI
import com.grigorevmp.habits.presentation.screen.today.data.HabitWithDatesUI

@Composable
fun HabitsForDayCard(
    habitsForDate: HabitWithDatesUI,
    habitViewModel: HabitViewModel?
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {

        Text(
            text = habitsForDate.date,
            fontSize = 20.sp,
            modifier = Modifier.padding(8.dp)
        )

        if (habitsForDate.habits.isEmpty()) {
            NoHabitsForDayCard()
        }

        for (habit in habitsForDate.habits) {
            var habitType by remember { mutableStateOf(habit.type) }

            habitType = HabitForDayCompletedSubCard(habitType, habitViewModel, habit)
        }

        Spacer(modifier = Modifier.padding(4.dp))
    }
}


@Preview(showBackground = true)
@Composable
fun HabitsForDateCardPreview() {
    HabitsForDayCard(
        habitsForDate = HabitWithDatesUI(
            habits = mutableListOf(
                HabitEntityUI(
                    id = 0,
                    dateId = 0,
                    title = "Title",
                    description = "Description",
                    type = HabitType.Missed,
                ),
                HabitEntityUI(
                    id = 0,
                    dateId = 0,
                    title = "Title 1",
                    description = "Description",
                    type = HabitType.Missed,
                ),
            ),
            date = "2023-10-28",
        ),
        habitViewModel = null,
    )
}


@Preview(showBackground = true)
@Composable
fun HabitsForDateCardEmpty2Preview() {
    HabitsForDayCard(
        habitsForDate = HabitWithDatesUI(
            habits = mutableListOf(),
            date = "2023-10-28",
        ),
        habitViewModel = null,
    )
}