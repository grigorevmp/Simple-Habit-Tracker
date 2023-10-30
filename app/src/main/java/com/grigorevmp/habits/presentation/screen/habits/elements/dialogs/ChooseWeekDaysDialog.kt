package com.grigorevmp.habits.presentation.screen.habits.elements.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grigorevmp.habits.presentation.screen.habits.elements.base.DateChip
import java.time.DayOfWeek

@OptIn(ExperimentalLayoutApi::class)
@ExperimentalMaterial3Api
@Composable
fun ChooseWeekDaysDialog(
    daysForHabit: Array<DayOfWeek>,
    onDaysSelected: (List<DayOfWeek>) -> Unit,
    hideDialog: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 20.dp, bottom = 16.dp), text = "Set days for habit", fontSize = 24.sp
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(top = 0.dp)
        ) {
            DayOfWeek.values().forEach {
                DateChip(it, daysForHabit, onDaysSelected)
            }
        }

        Button(
            onClick = {
                hideDialog()
            },
            modifier = Modifier
                .align(Alignment.End)
                .padding(vertical = 16.dp),
        ) {
            Text("Save")
        }
    }
}

@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
fun ChooseWeekDaysDialogPreview() {
    ChooseWeekDaysDialog(
        daysForHabit = arrayOf(DayOfWeek.MONDAY),
        onDaysSelected = { },
        hideDialog = { },
    )
}