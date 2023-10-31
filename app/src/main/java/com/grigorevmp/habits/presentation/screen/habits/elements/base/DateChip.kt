package com.grigorevmp.habits.presentation.screen.habits.elements.base

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateChip(
    day: DayOfWeek,
    selectedDays: Array<DayOfWeek>,
    onDaysSelected: (List<DayOfWeek>) -> Unit
) {
    var selected by remember { mutableStateOf(day in selectedDays) }

    FilterChip(
        modifier = Modifier.padding(end = 3.dp),
        onClick = {
            selected = !selected
            val updatedDays = if (selected) {
                selectedDays.toList() + day
            } else {
                selectedDays.toList() - day
            }
            onDaysSelected(updatedDays.toList())
        },
        label = {
            Text(day.name)
        },
        selected = selected,
        leadingIcon = if (selected) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done icon",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        },
    )
}