package com.grigorevmp.habits.presentation.screen.habits.elements.dialogs

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.grigorevmp.habits.data.SerializableTimePickerState
import com.grigorevmp.habits.presentation.screen.today.HabitViewModel
import java.time.DayOfWeek


@ExperimentalMaterial3Api
@Composable
fun AddHabitDialog(habitViewModel: HabitViewModel?, hideDialog: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var timePickerState by remember { mutableStateOf(SerializableTimePickerState(0, 0)) }
    var useAlert by remember { mutableStateOf(false) }
    var daysForHabit by remember { mutableStateOf(DayOfWeek.values()) }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 20.dp, bottom = 16.dp), text = "Add new habit", fontSize = 24.sp
        )

        var isError by rememberSaveable { mutableStateOf(false) }

        fun validate(text: String): Boolean {
            isError = text.isBlank()
            return !isError
        }

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            shape = RoundedCornerShape(8.dp),
            value = title,
            isError = isError,
            supportingText = {
                if (isError) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Enter something",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            trailingIcon = {
                if (isError) Icon(
                    Icons.Filled.Info, "error", tint = MaterialTheme.colorScheme.error
                )
            },
            onValueChange = { newValue -> title = newValue },
            label = { Text("Title") },
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
            label = { Text("Description") },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
            ),
        )


        var timeDialogShown by remember { mutableStateOf(false) }
        var habitDaysShown by remember { mutableStateOf(false) }

        if (timeDialogShown) {
            Dialog(onDismissRequest = { timeDialogShown = false }) {
                Surface(
                    modifier = Modifier.clip(RoundedCornerShape(12.dp)),
                    color = MaterialTheme.colorScheme.surfaceContainerHighest
                ) {
                    ChooseTimeDialog(
                        timePickerState,
                        useAlert,
                        { enabled -> useAlert = enabled },
                        { time -> timePickerState = time },
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
                    ChooseWeekDaysDialog(daysForHabit, { listWithDays ->
                        daysForHabit = listWithDays.toTypedArray()
                    }) { habitDaysShown = false }
                }
            }
        }

        Card(
            modifier = Modifier
                .padding(top = 12.dp)
                .align(Alignment.End),
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
                Icon(Icons.Filled.DateRange, contentDescription = "Days")
                Text(
                    text = "Edit days for habit",
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }

        Card(
            modifier = Modifier
                .padding(top = 12.dp)
                .align(Alignment.End),
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
                Icon(Icons.Filled.Notifications, contentDescription = "Reminder")
                Text(
                    text = if (!useAlert) "Add notification" else "Change notification",
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
                habitViewModel?.addHabit(
                    context,
                    title,
                    description,
                    daysForHabit,
                    useAlert,
                    SerializableTimePickerState(
                        hour = timePickerState.hour,
                        minute = timePickerState.minute,
                    ),
                )
            }
        }) {
            Text("Add Habit")
        }
    }
}

@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
fun AddHabitDialogPreview() {
    AddHabitDialog(
        habitViewModel = null,
        hideDialog = { },
    )
}