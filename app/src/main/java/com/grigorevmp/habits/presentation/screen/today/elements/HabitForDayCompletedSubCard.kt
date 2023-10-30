package com.grigorevmp.habits.presentation.screen.today.elements

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grigorevmp.habits.R
import com.grigorevmp.habits.data.habit.HabitType
import com.grigorevmp.habits.presentation.screen.today.HabitViewModel
import com.grigorevmp.habits.presentation.screen.today.data.HabitEntityUI
import com.grigorevmp.habits.presentation.screen.today.data.HabitWithDatesUI

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun HabitForDayCompletedSubCard(
    habitType: HabitType,
    habitViewModel: HabitViewModel?,
    habit: HabitEntityUI
): HabitType {
    var habitType1 = habitType
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        modifier = Modifier
            .combinedClickable(
                onClick = {
                    habitType1 = if (habitType1 != HabitType.Unknown) {
                        HabitType.Unknown
                    } else {
                        HabitType.Done
                    }
                    habitViewModel?.updateHabitRef(habit.dateId, habit.id, habitType1)
                },
                onLongClick = {
                    habitType1 = if (habitType1 != HabitType.Missed) {
                        HabitType.Missed
                    } else {
                        HabitType.Done
                    }
                    habitViewModel?.updateHabitRef(habit.dateId, habit.id, habitType1)
                },
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val iconResource = when (habitType1) {
                HabitType.Done -> {
                    R.drawable.ic_done
                }

                HabitType.Unknown -> {
                    R.drawable.ic_empty
                }

                HabitType.Missed -> {
                    R.drawable.ic_clear
                }
            }

            Icon(
                painter = painterResource(id = iconResource),
                contentDescription = "Reminder",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )

            Text(
                text = habit.title,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 8.dp),
            )
        }
    }
    return habitType1
}


@Preview(showBackground = true)
@Composable
fun HabitForDayCompletedSubCardPreview() {
    HabitForDayCompletedSubCard(
        habitType = HabitType.Missed,
        habitViewModel = null,
        habit = HabitEntityUI (
            id = 0,
            dateId = 0,
            title = "Title",
            description = "Description",
            type = HabitType.Missed,
        ),
    )
}