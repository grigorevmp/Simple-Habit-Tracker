package com.grigorevmp.habits.presentation.screen.habits.elements.bottom_sheet.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grigorevmp.habits.presentation.screen.habits.parseToDate
import java.time.DayOfWeek

@OptIn(ExperimentalLayoutApi::class)
@ExperimentalMaterial3Api
@Composable
fun WeekDaysFlowComponent(
    daysForHabit: Array<DayOfWeek>,
    modifier: Modifier = Modifier,
    onDaysSelected: (List<DayOfWeek>) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(top = 0.dp)
        ) {
            DayOfWeek.entries.forEachIndexed { index, day ->
                DayItem(
                    isSelected = day in daysForHabit,
                    daysForHabit = daysForHabit,
                    day = day,
                    onDaysSelected = onDaysSelected,
                )

                if (index != DayOfWeek.entries.toTypedArray().lastIndex) {
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun DayItem(
    isSelected: Boolean,
    daysForHabit: Array<DayOfWeek>,
    day: DayOfWeek,
    onDaysSelected: (List<DayOfWeek>) -> Unit
) {
    val selected = remember { mutableStateOf(isSelected) }
    val color = animateColorAsState(
        if (selected.value) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
        label = "dayCircleColor"
    )

    Box(
        modifier = Modifier.size(36.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = CircleShape,
            modifier = Modifier.size(36.dp),
            color = color.value,
            onClick = {
                selected.value = !selected.value
                val updatedDays = if (selected.value) {
                    daysForHabit.toList() + day
                } else {
                    daysForHabit.toList() - day
                }
                onDaysSelected(updatedDays.toList())
            },
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day.parseToDate(LocalContext.current),
                    textAlign = TextAlign.Center,
                    style = TextStyle(fontSize = 12.sp),
                )
            }
        }
    }
}

@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
fun ChooseWeekDaysDialogPreview() {
    WeekDaysFlowComponent(
        daysForHabit = arrayOf(DayOfWeek.MONDAY),
    ) { }
}