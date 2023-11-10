package com.grigorevmp.habits.presentation.screen.habits.elements

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grigorevmp.habits.R
import com.grigorevmp.habits.core.utils.Utils
import com.grigorevmp.habits.data.CountableEntity
import com.grigorevmp.habits.data.HabitEntity
import com.grigorevmp.habits.data.SerializableTimePickerState
import com.grigorevmp.habits.presentation.screen.habits.elements.bottom_sheet.AddEditBottomSheet
import com.grigorevmp.habits.presentation.screen.habits.parseToDate
import com.grigorevmp.habits.presentation.screen.habits.toReadableForms
import java.time.DayOfWeek

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitCard(
    habitEntity: HabitEntity,
    updateHabitEntity: (Context, HabitEntity) -> Unit,
    deleteHabitEntity: (HabitEntity) -> Unit,
) {
    val title = habitEntity.title
    val description = habitEntity.description
    val alertEnabled = habitEntity.alertEnabled
    val countable = habitEntity.countable
    val countableEntity = habitEntity.countableEntity
    val days = habitEntity.days.parseToDate(LocalContext.current)
    val time = habitEntity.time.toReadableForms()

    var selectedHabit by remember { mutableStateOf<HabitEntity?>(null) }


    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded  = true)

    selectedHabit?.also {
        AddEditBottomSheet(
            openBottomSheet, bottomSheetState,
            habitEntity = it,
            updateHabitEntity = updateHabitEntity,
            deleteHabitEntity = deleteHabitEntity,
        ) { state ->
            openBottomSheet = state
        }
    }

    Card(
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .fillMaxWidth(),
        onClick = {
            selectedHabit = habitEntity
            openBottomSheet = true
        }
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = title, fontSize = 20.sp, modifier = Modifier.padding(top = 4.dp)
            )

            if (description.isNotBlank()) {
                Text(text = description, modifier = Modifier.padding(top = 4.dp))
            }

            Card(
                modifier = Modifier.padding(top = 12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiary
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.DateRange,
                        contentDescription = "Reminder",

                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    )

                    Text(
                        text = days,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(start = 8.dp),
                    )
                }
            }

            if (countable && countableEntity != null) {
                Card(
                    modifier = Modifier.padding(top = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiary
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.Info,
                            contentDescription = "Reminder",

                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                        )

                        Text(
                            text = "${countableEntity.actionName} ${countableEntity.targetValue} ${countableEntity.valueName}",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(start = 8.dp),
                        )
                    }
                }
            }

            if (alertEnabled) {
                Card(
                    modifier = Modifier.padding(top = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.Notifications,
                            contentDescription = "Reminder",

                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                        )

                        Text(
                            text = stringResource(R.string.habit_screen_remind_at, time),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(start = 8.dp),
                        )

                        Spacer(modifier = Modifier.weight(1f))


                        val requestCode =
                            (habitEntity.id * 100).toInt() + habitEntity.days[0].ordinal

                        if (!Utils.checkIfPendingIntentIsRegistered(
                                LocalContext.current, requestCode
                            )
                        ) {
                            Icon(
                                Icons.Filled.Warning,
                                contentDescription = "Failed",

                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HabitCardPreview() {
    HabitCard(
        habitEntity = HabitEntity(
            id = 0,
            title = "Title",
            description = "Description",
            days = arrayOf(DayOfWeek.FRIDAY),
            alertEnabled = true,
            time = SerializableTimePickerState(10, 10),
            completed = false,
            countable = true,
            countableEntity = CountableEntity(
                30, "Bla-bla", "Do"
            )
        ),
        updateHabitEntity = { _, _ -> },
        deleteHabitEntity = { _ -> },
    )
}