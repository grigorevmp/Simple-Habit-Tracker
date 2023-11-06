package com.grigorevmp.habits.presentation.screen.today.elements

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.grigorevmp.habits.R
import com.grigorevmp.habits.data.habit.HabitType
import com.grigorevmp.habits.presentation.screen.today.data.HabitEntityUI
import com.grigorevmp.habits.presentation.screen.today.dialogs.SetCountableFoHabit

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun HabitForDayCompletedSubCard(
    updateHabitRef: (Long, Long, HabitType) -> Unit,
    updateHabitRefCountable: (Long, Long, HabitType, Int) -> Unit,
    habit: HabitEntityUI,
    setNewHabitType: (HabitType) -> Unit
) {
    val habitTypeMutable = remember(habit.type.ordinal) { mutableStateOf(habit.type) }
    val countableDialog = remember {
        mutableStateOf(false)
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        modifier = Modifier
            .combinedClickable(
                onClick = {
                    if (habit.countable) {
                        countableDialog.value = true

                    } else {
                        habitTypeMutable.value = if (habitTypeMutable.value != HabitType.Unknown) {
                            HabitType.Unknown
                        } else {
                            HabitType.Done
                        }
                        updateHabitRef(habit.dateId, habit.id, habitTypeMutable.value)
                    }
                },
                onLongClick = {
                    if (habit.countable) {
                        countableDialog.value = true

                    } else {
                        habitTypeMutable.value = if (habitTypeMutable.value != HabitType.Missed) {
                            HabitType.Missed
                        } else {
                            HabitType.Done
                        }
                        updateHabitRef(habit.dateId, habit.id, habitTypeMutable.value)
                    }
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
            val iconResource = when (habitTypeMutable.value) {
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
                contentDescription = stringResource(R.string.today_screen_habit_state_icon_description),
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )

            val additionalText = if (habit.countable) " (${habit.value}/${habit.maxValue} ${habit.valueName})" else ""

            Text(
                text = habit.title + additionalText,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 8.dp),
            )
        }
    }

    if (countableDialog.value) {
        Dialog(onDismissRequest = { countableDialog.value = false }) {
            Surface(
                modifier = Modifier.clip(RoundedCornerShape(12.dp)),
                color = MaterialTheme.colorScheme.surfaceContainerHighest
            ) {
                SetCountableFoHabit(
                    habit,
                    { value: Int, isCompleted: Boolean ->
                        habitTypeMutable.value = if (isCompleted) {
                            HabitType.Done
                        } else {
                            HabitType.Unknown
                        }

                        habit.value = value

                        updateHabitRefCountable(
                            habit.dateId,
                            habit.id,
                            habitTypeMutable.value,
                            value
                        )
                    }
                ) {
                    countableDialog.value = false
                }
            }
        }
    }

    setNewHabitType(habitTypeMutable.value)
}


@Preview(showBackground = true)
@Composable
fun HabitForDayCompletedSubCardPreview() {
    HabitForDayCompletedSubCard(
        updateHabitRef = { _, _, _ -> },
        habit = HabitEntityUI(
            id = 0,
            dateId = 0,
            title = "Title",
            description = "Description",
            type = HabitType.Missed,
        ),
        setNewHabitType = { _ -> },
        updateHabitRefCountable = { _, _, _, _ -> },
    )
}