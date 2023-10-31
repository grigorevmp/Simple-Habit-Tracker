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
import com.grigorevmp.habits.presentation.screen.today.data.HabitEntityUI

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun HabitForDayCompletedSubCard(
    habitType: HabitType,
    updateHabitRef: (Long, Long, HabitType) -> Unit,
    habit: HabitEntityUI,
    setNewHabitType: (HabitType) -> Unit
) {
    var habitTypeMutable = habitType

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        modifier = Modifier
            .combinedClickable(
                onClick = {
                    habitTypeMutable = if (habitTypeMutable != HabitType.Unknown) {
                        HabitType.Unknown
                    } else {
                        HabitType.Done
                    }
                    updateHabitRef(habit.dateId, habit.id, habitTypeMutable)
                },
                onLongClick = {
                    habitTypeMutable = if (habitTypeMutable != HabitType.Missed) {
                        HabitType.Missed
                    } else {
                        HabitType.Done
                    }
                    updateHabitRef(habit.dateId, habit.id, habitTypeMutable)
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
            val iconResource = when (habitTypeMutable) {
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

    setNewHabitType(habitTypeMutable)
}


@Preview(showBackground = true)
@Composable
fun HabitForDayCompletedSubCardPreview() {
    HabitForDayCompletedSubCard(
        habitType = HabitType.Missed,
        updateHabitRef = { _, _, _ -> },
        habit = HabitEntityUI(
            id = 0,
            dateId = 0,
            title = "Title",
            description = "Description",
            type = HabitType.Missed,
        ),
        setNewHabitType = { _ -> }
    )
}