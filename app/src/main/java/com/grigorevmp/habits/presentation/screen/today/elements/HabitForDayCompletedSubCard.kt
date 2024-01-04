package com.grigorevmp.habits.presentation.screen.today.elements

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.grigorevmp.habits.R
import com.grigorevmp.habits.data.HabitCategory
import com.grigorevmp.habits.data.SerializableTimePickerState
import com.grigorevmp.habits.data.habit.HabitType
import com.grigorevmp.habits.presentation.screen.today.data.HabitEntityUI
import com.grigorevmp.habits.presentation.screen.today.dialogs.SetCountableFoHabit
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun HabitForDayCompletedSubCard(
    updateHabitRef: (Long, Long, HabitType) -> Unit,
    updateHabitRefCountable: (Long, Long, HabitType, Int) -> Unit,
    habit: HabitEntityUI,
    setNewHabitType: (HabitType) -> Unit
) {
    var habitTypeMutable by remember { mutableStateOf(habit.type) }
    val countableDialog = remember {
        mutableStateOf(false)
    }

    val haptics = LocalHapticFeedback.current

    val formatterHour = DateTimeFormatter.ofPattern("HH")
    val realHour = LocalDateTime.now().format(formatterHour)

    val formatterMinute = DateTimeFormatter.ofPattern("mm")
    val realMinute = LocalDateTime.now().format(formatterMinute)

    val containerColor =
        if ((habit.type != HabitType.Done) && ((habit.time.hour < realHour.toInt()) || (habit.time.hour == realHour.toInt() && habit.time.minute < realMinute.toInt()))) {
            MaterialTheme.colorScheme.error
        } else {
            MaterialTheme.colorScheme.primary
        }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        modifier = Modifier
            .combinedClickable(
                onClick = {
                    if (habit.countable) {
                        countableDialog.value = true
                    } else {
                        habitTypeMutable =
                            if (habit.type != HabitType.Unknown) {
                                HabitType.Unknown
                            } else {
                                HabitType.Done
                            }
                        updateHabitRef(habit.dateId, habit.id, habitTypeMutable)
                    }
                },
                onLongClick = {
                    if (habit.countable) {
                        countableDialog.value = true
                    } else {
                        habitTypeMutable = if (habit.type != HabitType.Missed) {
                            HabitType.Missed
                        } else {
                            HabitType.Done
                        }
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        updateHabitRef(habit.dateId, habit.id, habitTypeMutable)
                    }
                },
            )
            .padding(horizontal = 16.dp, vertical = 4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val iconResource = when (habit.type) {
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

            val additionalText =
                if (habit.countable) " (${habit.value}/${habit.maxValue} ${habit.valueName})" else ""

            Text(
                text = habit.title + additionalText,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(start = 8.dp)
            )

            if (habit.category != HabitCategory.None.name) {
                Spacer(modifier = Modifier.weight(1f))

                Card {
                    Text(
                        text = getHabitNameValue(habit.category),
                        modifier = Modifier.padding(
                            vertical = 8.dp,
                            horizontal = 16.dp
                        )
                    )
                }
            }
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
                        habitTypeMutable = if (isCompleted) {
                            HabitType.Done
                        } else {
                            HabitType.Unknown
                        }

                        habit.value = value

                        updateHabitRefCountable(
                            habit.dateId,
                            habit.id,
                            habitTypeMutable,
                            value
                        )
                    }
                ) {
                    countableDialog.value = false
                }
            }
        }
    }

    setNewHabitType(habitTypeMutable)
}

@Composable
private fun getHabitNameValue(
    habitName: String
) = when (HabitCategory.valueOf(habitName)) {
    HabitCategory.None -> stringResource(R.string.habit_property_category_none)
    HabitCategory.Food -> stringResource(R.string.habit_property_category_food)
    HabitCategory.PhysicalActivity -> stringResource(R.string.habit_property_category_sport)
    HabitCategory.Relaxation -> stringResource(R.string.habit_property_category_relax)
    HabitCategory.Meditation -> stringResource(R.string.habit_property_category_meditation)
    HabitCategory.BadHabits -> stringResource(R.string.habit_property_category_bad)
    HabitCategory.Reading -> stringResource(R.string.habit_property_category_reading)
    HabitCategory.Education -> stringResource(R.string.habit_property_category_education)
    HabitCategory.Languages -> stringResource(R.string.habit_property_category_lang)
    HabitCategory.Skills -> stringResource(R.string.habit_property_category_skills)
    HabitCategory.Planning -> stringResource(R.string.habit_property_category_planning)
    HabitCategory.Working -> stringResource(R.string.habit_property_category_working)
    HabitCategory.Diary -> stringResource(R.string.habit_property_category_diary)
    HabitCategory.StressFighting -> stringResource(R.string.habit_property_category_tress)
    HabitCategory.Communication -> stringResource(R.string.habit_property_category_comm)
    HabitCategory.SelfTime -> stringResource(R.string.habit_property_category_self)
    HabitCategory.Productivity -> stringResource(R.string.habit_property_category_productivity)
    HabitCategory.WorkBalance -> stringResource(R.string.habit_property_category_work_life_balance)
    HabitCategory.FinanceControl -> stringResource(R.string.habit_property_category_finance_control)
    HabitCategory.Budget -> stringResource(R.string.habit_property_category_budget)
    HabitCategory.Hobby -> stringResource(R.string.habit_property_category_hobby)
    HabitCategory.Cleaning -> stringResource(R.string.habit_property_category_cleaning)
    HabitCategory.Cooking -> stringResource(R.string.habit_property_category__cooking)
    HabitCategory.PetTime -> stringResource(R.string.habit_property_category_pet_time)
    HabitCategory.Personal -> stringResource(R.string.habit_property_category_personal)
    HabitCategory.Volunteering -> stringResource(R.string.habit_property_category_volunteering)
    HabitCategory.FriendsTime -> stringResource(R.string.habit_property_category_friends)
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
            category = HabitCategory.None.name,
            alert = true,
            time = SerializableTimePickerState(14, 0),
            type = HabitType.Missed,
        ),
        setNewHabitType = { _ -> },
        updateHabitRefCountable = { _, _, _, _ -> },
    )
}