package com.grigorevmp.habits.presentation.screen.habits.elements.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.grigorevmp.habits.R
import com.grigorevmp.habits.data.CountableEntity

@Composable
fun SetCountableValueDialog(
    countableEntity: CountableEntity?,
    setCountableEntity: (CountableEntity?) -> Unit,
    closeDialog: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 20.dp, bottom = 16.dp),
            text = stringResource(R.string.habit_screen_countable_dialog_title),
            fontSize = 24.sp
        )

        val isCountable = countableEntity != null

        var valueName by rememberSaveable { mutableStateOf(countableEntity?.valueName ?: "") }
        var valueAction by rememberSaveable { mutableStateOf(countableEntity?.actionName ?: "") }
        var maxValue by rememberSaveable { mutableIntStateOf(countableEntity?.targetValue ?: 0) }
        var maxValueStr by rememberSaveable { mutableStateOf(maxValue.toString()) }

        TextField(
            value = valueAction,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            shape = RoundedCornerShape(8.dp),
            onValueChange = { newValue: String -> valueAction = newValue },
            label = { Text(stringResource(R.string.habit_screen_countable_dialog_value_action_label)) },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
            ),
        )

        TextField(
            value = maxValueStr,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            shape = RoundedCornerShape(8.dp),
            label = { Text(stringResource(R.string.habit_screen_countable_dialog_max_value_label)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = { newValue: String ->
                maxValue = if (newValue.isNotBlank() && newValue.isDigitsOnly()) {
                    newValue.toInt()
                } else {
                    0
                }
                maxValueStr = newValue
            },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
            ),
        )

        TextField(
            value = valueName,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            shape = RoundedCornerShape(8.dp),
            onValueChange = { newValue: String -> valueName = newValue },
            label = { Text(stringResource(R.string.habit_screen_countable_dialog_value_name_label)) },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
            ),
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
        ) {
            Row {
                Icon(
                    Icons.Outlined.Info,
                    contentDescription = stringResource(R.string.habit_screen_countable_dialog_info_icon_description),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 8.dp),
                )

                Text(
                    text = "$valueAction $maxValue $valueName",
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterVertically),
                )
            }
        }

        if (isCountable) {
            Button(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                onClick = {
                    setCountableEntity(null)
                    closeDialog()
                }
            ) {
                Text(stringResource(R.string.habit_screen_countable_dialog_remove_button))
            }
        }

        Row(
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Button(modifier = Modifier.padding(vertical = 16.dp),
                onClick = {
                    closeDialog()
                }
            ) {
                Text(stringResource(R.string.habit_screen_countable_dialog_cancel_button))
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(modifier = Modifier
                .padding(vertical = 16.dp), onClick = {
                setCountableEntity(
                    CountableEntity(
                        actionName = valueAction,
                        valueName = valueName,
                        targetValue = maxValue,
                    )
                )
                closeDialog()
            }) {
                Text(stringResource(R.string.habit_screen_countable_dialog_save_button))
            }
        }

    }
}


@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
fun SetCountableValueDialogPreview() {
    SetCountableValueDialog(
        countableEntity = null,
        setCountableEntity = { }
    ) { }
}