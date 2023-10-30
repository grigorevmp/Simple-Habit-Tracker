package com.grigorevmp.habits.presentation.screen.habits.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.grigorevmp.habits.data.HabitEntity
import com.grigorevmp.habits.presentation.screen.habits.elements.dialogs.EditHabitDialog
import com.grigorevmp.habits.presentation.screen.habits.parseToDate
import com.grigorevmp.habits.presentation.screen.habits.toReadableForms
import com.grigorevmp.habits.presentation.screen.today.HabitViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitCard(
    vm: HabitViewModel?,
    habitEntity: HabitEntity
) {
    val title = habitEntity.title
    val description = habitEntity.description
    val alertEnabled = habitEntity.alertEnabled
    val days = habitEntity.days.parseToDate()
    val time = habitEntity.time.toReadableForms()

    var showingDialog by remember { mutableStateOf(false) }
    var selectedHabit by remember { mutableStateOf<HabitEntity?>(null) }

    Card(
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .fillMaxWidth(),
        onClick = {
            selectedHabit = habitEntity
            showingDialog = true
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
                            Icons.Filled.Notifications,
                            contentDescription = "Reminder",

                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                        )

                        Text(
                            text = "Напомним в $time",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(start = 8.dp),
                        )
                    }
                }
            }

        }
    }

    selectedHabit?.let {
        if (showingDialog) {
            Dialog(onDismissRequest = { showingDialog = false }) {
                Surface(
                    modifier = Modifier.clip(RoundedCornerShape(12.dp)),
                    color = MaterialTheme.colorScheme.surfaceContainerHighest
                ) {
                    EditHabitDialog(it, vm, { item ->
                        vm?.deleteHabit(item)
                    }) {
                        showingDialog = false
                    }
                }
            }
        }
    }
}
