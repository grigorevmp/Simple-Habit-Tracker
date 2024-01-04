package com.grigorevmp.habits.presentation.screen.today.elements

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grigorevmp.habits.R
import com.grigorevmp.habits.data.HabitCategory
import com.grigorevmp.habits.data.SerializableTimePickerState
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

        val habitsUnplanned = habitsForDate.habits.filter { !it.alert }
        val habitsPlanned = habitsForDate.habits.filter { it.alert }
            .sortedBy { it.time.minute }
            .sortedBy { it.time.hour }
            .groupBy { Pair(it.time.hour, it.time.minute) }

        if (habitsPlanned.isNotEmpty()) {
            Text(
                text = stringResource(R.string.habit_screen_habit_card_planned),
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )

            for ((time, habits) in habitsPlanned.entries) {
                val hour = if (time.first > 9) time.first.toString() else "0${time.first}"
                val minute = if (time.second > 9) time.second.toString() else "0${time.second}"

                Text(
                    text = "$hour:$minute",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 16.dp, bottom = 4.dp, top = 4.dp)
                )

                for (habit in habits) {
                    HabitForDayCompletedSubCard(
                        updateHabitRef = updateHabitRef,
                        updateHabitRefCountable = updateHabitRefCountable,
                        habit = habit,
                    ) { newHabitType: HabitType ->
                        habit.type = newHabitType
                    }
                }
            }

        }

        if (habitsUnplanned.isNotEmpty()) {
            Text(
                text = stringResource(R.string.habit_screen_habit_card_unplanned),
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
            )

            for (habit in habitsUnplanned) {
                HabitForDayCompletedSubCard(
                    updateHabitRef = updateHabitRef,
                    updateHabitRefCountable = updateHabitRefCountable,
                    habit = habit,
                ) { newHabitType: HabitType ->
                    habit.type = newHabitType
                }
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
                    category = HabitCategory.None.name,
                    alert = false,
                    time = SerializableTimePickerState(0, 0),
                    type = HabitType.Missed,
                ),
                HabitEntityUI(
                    id = 0,
                    dateId = 0,
                    title = "Title 1",
                    description = "Description",
                    category = HabitCategory.None.name,
                    alert = false,
                    time = SerializableTimePickerState(0, 0),
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