package com.grigorevmp.habits.presentation.screen.habits.elements.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grigorevmp.habits.R
import com.grigorevmp.habits.data.SerializableTimePickerState

@ExperimentalMaterial3Api
@Composable
fun ChooseTimeDialog(
    timePickerState: SerializableTimePickerState,
    useAlert: Boolean,
    setUpNotificationAlert: (Boolean) -> Unit,
    setUpNotification: (SerializableTimePickerState) -> Unit,
    hideDialog: () -> Unit
) {
    val timeState = rememberTimePickerState(
        initialHour = timePickerState.hour,
        initialMinute = timePickerState.minute
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 20.dp, bottom = 16.dp), text = stringResource(R.string.habit_screen_choose_time_set_notification_title), fontSize = 24.sp
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            TimeInput(
                state = timeState,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .align(Alignment.CenterHorizontally),
            )
        }

        Row(
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            if (useAlert) {
                Button(modifier = Modifier.padding(start = 8.dp, top = 10.dp), onClick = {
                    setUpNotificationAlert(false)
                    hideDialog()
                }) {
                    Text(stringResource(R.string.habit_screen_choose_time_remove_button))
                }
            } else {
                Button(onClick = {
                    hideDialog()
                }) {
                    Text(stringResource(R.string.habit_screen_choose_time_cancel_button))
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(onClick = {
                setUpNotificationAlert(true)
                setUpNotification(SerializableTimePickerState(timeState.hour, timeState.minute))
                hideDialog()
            }) {
                Text(stringResource(R.string.habit_screen_choose_time_save_button))
            }
        }
    }
}

@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
fun ChooseTimeDialogPreview() {
    ChooseTimeDialog(
        timePickerState = SerializableTimePickerState(10, 11),
        useAlert = false,
        setUpNotificationAlert = { },
        setUpNotification = { },
        hideDialog = { },
    )
}