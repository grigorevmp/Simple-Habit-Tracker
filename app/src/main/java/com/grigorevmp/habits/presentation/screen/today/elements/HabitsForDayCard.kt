package com.grigorevmp.habits.presentation.screen.today.elements

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grigorevmp.habits.data.habit.HabitType
import com.grigorevmp.habits.presentation.screen.today.data.HabitEntityUI
import com.grigorevmp.habits.presentation.screen.today.data.HabitWithDatesUI

@Composable
fun HabitsForDayCard(
    habitsForDate: HabitWithDatesUI,
    updateHabitRef: (Long, Long, HabitType) -> Unit,
    updateHabitRefCountable: (Long, Long, HabitType, Int) -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {

        Text(
            text = habitsForDate.date,
            fontSize = 20.sp,
            modifier = Modifier.padding(16.dp)
        )

        if (habitsForDate.habits.isEmpty()) {
            NoHabitsForDayCard()
        }

        for (habit in habitsForDate.habits) {
            HabitForDayCompletedSubCard(
                updateHabitRef = updateHabitRef,
                updateHabitRefCountable = updateHabitRefCountable,
                habit = habit,
            ) { newHabitType: HabitType ->
                habit.type = newHabitType
            }
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
        updateHabitRef = { _, _, _ -> },
        updateHabitRefCountable = { _, _, _, _ -> },
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
        updateHabitRef = { _, _, _ -> },
        updateHabitRefCountable = { _, _, _, _ -> },
    )
}