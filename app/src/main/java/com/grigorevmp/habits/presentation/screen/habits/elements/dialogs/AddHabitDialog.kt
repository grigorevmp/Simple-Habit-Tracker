package com.grigorevmp.habits.presentation.screen.habits.elements.dialogs

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.grigorevmp.habits.R
import com.grigorevmp.habits.data.CountableEntity
import com.grigorevmp.habits.data.SerializableTimePickerState
import java.time.DayOfWeek


@ExperimentalMaterial3Api
@Composable
fun AddHabitDialog(
    addHabitEntity: (Context, String, String, Array<DayOfWeek>, Boolean, SerializableTimePickerState, Boolean, CountableEntity?) -> Unit,
    hideDialog: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var timePickerState by remember { mutableStateOf(SerializableTimePickerState(0, 0)) }
    var useAlert by remember { mutableStateOf(false) }
    var daysForHabit by remember { mutableStateOf(DayOfWeek.values()) }

    var countableEntity by remember { mutableStateOf<CountableEntity?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 20.dp, bottom = 8.dp),
            text = stringResource(R.string.habit_screen_add_new_habit_dialog_title),
            fontSize = 24.sp
        )

        var isError by rememberSaveable { mutableStateOf(false) }

        fun validate(text: String): Boolean {
            isError = text.isBlank()
            return !isError
        }

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(8.dp),
            value = title,
            isError = isError,
            supportingText = {
                if (isError) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.habit_screen_add_new_habit_enter_something_error),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            trailingIcon = {
                if (isError) Icon(
                    Icons.Filled.Info,
                    stringResource(R.string.habit_screen_add_new_habit_error_icon_description), tint = MaterialTheme.colorScheme.error
                )
            },
            onValueChange = { newValue -> title = newValue },
            label = { Text(stringResource(R.string.habit_screen_add_new_habit_title)) },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
            ),
        )

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            shape = RoundedCornerShape(8.dp),
            value = description,
            onValueChange = { newValue -> description = newValue },
            label = { Text(stringResource(R.string.habit_screen_add_new_habit_description)) },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
            ),
        )


        var timeDialogShown by remember { mutableStateOf(false) }
        var habitDaysShown by remember { mutableStateOf(false) }
        var countableShown by remember { mutableStateOf(false) }

        if (timeDialogShown) {
            Dialog(onDismissRequest = { timeDialogShown = false }) {
                Surface(
                    modifier = Modifier.clip(RoundedCornerShape(12.dp)),
                    color = MaterialTheme.colorScheme.surfaceContainerHighest
                ) {
                    ChooseTimeDialog(
                        timePickerState = timePickerState,
                        useAlert = useAlert,
                        setUpNotificationAlert = { enabled -> useAlert = enabled },
                        setUpNotification = { time -> timePickerState = time },
                    ) { timeDialogShown = false }
                }
            }
        }

        if (habitDaysShown) {
            Dialog(onDismissRequest = { habitDaysShown = false }) {
                Surface(
                    modifier = Modifier.clip(RoundedCornerShape(12.dp)),
                    color = MaterialTheme.colorScheme.surfaceContainerHighest
                ) {
                    ChooseWeekDaysDialog(
                        daysForHabit = daysForHabit,
                        onDaysSelected = { listWithDays ->
                            daysForHabit = listWithDays.toTypedArray()
                        }
                    ) { habitDaysShown = false }
                }
            }
        }

        if (countableShown) {
            Dialog(onDismissRequest = { countableShown = false }) {
                Surface(
                    modifier = Modifier.clip(RoundedCornerShape(12.dp)),
                    color = MaterialTheme.colorScheme.surfaceContainerHighest
                ) {
                    SetCountableValueDialog(
                        countableEntity,
                        { newCountableEntity: CountableEntity? ->
                            countableEntity = newCountableEntity
                        }
                    ) { countableShown = false }
                }
            }
        }

        Card(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            onClick = {
                habitDaysShown = true
            }, colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Icons.Filled.DateRange, contentDescription = stringResource(R.string.habit_screen_add_new_habit_days_icon_description))
                Text(
                    text = stringResource(R.string.habit_screen_add_new_habit_edit_days_for_habit_button),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }

        Card(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            onClick = {
                timeDialogShown = true
            }, colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Icons.Filled.Notifications, contentDescription = stringResource(R.string.habit_screen_add_new_habit_reminder_icon_description))
                Text(
                    text = if (!useAlert) {
                        stringResource(R.string.habit_screen_add_new_habit_add_notification_button)
                    } else {
                        stringResource(
                            R.string.habit_screen_add_new_habit_change_notification_button
                        )
                    },
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }

        Card(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            onClick = {
                countableShown = true
            }, colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_countable),
                    contentDescription = stringResource(R.string.habit_screen_add_new_habit_dialog_countable_icon_description)
                )
                Text(
                    text = if (countableEntity == null) stringResource(id = R.string.habit_screen_add_new_habit_dialog_countable_new_button) else stringResource(id = R.string.habit_screen_add_new_habit_dialog_countable_change_icon),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }

        val context = LocalContext.current

        Button(modifier = Modifier
            .align(Alignment.End)
            .padding(vertical = 16.dp), onClick = {
            if (validate(title)) {
                hideDialog()
                addHabitEntity(
                    context,
                    title,
                    description,
                    daysForHabit,
                    useAlert,
                    SerializableTimePickerState(
                        hour = timePickerState.hour,
                        minute = timePickerState.minute,
                    ),
                    countableEntity != null,
                    countableEntity,
                )
            }
        }) {
            Text(stringResource(R.string.habit_screen_add_new_habit_add_habit_button))
        }
    }
}

@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
fun AddHabitDialogPreview() {
    AddHabitDialog(
        addHabitEntity = { _, _, _, _, _, _, _, _ -> },
        hideDialog = { },
    )
}