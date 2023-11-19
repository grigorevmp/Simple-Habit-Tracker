package com.grigorevmp.habits.presentation.screen.habits.elements.bottom_sheet.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
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
import androidx.core.text.isDigitsOnly
import com.grigorevmp.habits.R
import com.grigorevmp.habits.data.CountableEntity

@Composable
fun CountableValueComponent(
    countableEntity: CountableEntity?,
    setCountableEntity: (CountableEntity?) -> Unit
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        val action = stringResource(R.string.action_read_summary)
        val name = stringResource(R.string.action_read_name)

        var valueAction by rememberSaveable { mutableStateOf(countableEntity?.actionName ?: action) }
        var maxValue by rememberSaveable { mutableIntStateOf(countableEntity?.targetValue ?: 0) }
        var valueName by rememberSaveable { mutableStateOf(countableEntity?.valueName ?: name) }

        var maxValueStr by rememberSaveable { mutableStateOf(maxValue.toString()) }

        TextField(
            value = valueAction,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            shape = RoundedCornerShape(8.dp),
            onValueChange = { newValue: String ->
                valueAction = newValue
                setCountableEntity(
                    CountableEntity(
                        actionName = valueAction,
                        valueName = valueName,
                        targetValue = maxValue,
                    )
                )
            },
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
                setCountableEntity(
                    CountableEntity(
                        actionName = valueAction,
                        valueName = valueName,
                        targetValue = maxValue,
                    )
                )
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
            onValueChange = { newValue: String ->
                valueName = newValue
                setCountableEntity(
                    CountableEntity(
                        actionName = valueAction,
                        valueName = valueName,
                        targetValue = maxValue,
                    )
                )
            },
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
    }
}


@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
fun SetCountableValueDialogPreview() {
    CountableValueComponent(
        countableEntity = null
    ) { }
}