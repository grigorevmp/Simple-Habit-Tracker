package com.grigorevmp.habits.presentation.screen.habits

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.grigorevmp.habits.presentation.screen.habits.elements.AllHabitList
import com.grigorevmp.habits.presentation.screen.habits.elements.dialogs.AddHabitDialog
import com.grigorevmp.habits.presentation.screen.today.HabitViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitListModule(habitViewModel: HabitViewModel? = null) {
    var dialogShown by remember { mutableStateOf(false) }

    if (dialogShown) {
        Dialog(
            onDismissRequest = { dialogShown = false }
        ) {
            Surface(
                modifier = Modifier.clip(RoundedCornerShape(12.dp)),
                color = MaterialTheme.colorScheme.surfaceContainerHighest
            ) {
                AddHabitDialog(habitViewModel) {
                    dialogShown = false
                }
            }
        }
    }

    Surface(Modifier.fillMaxSize()) {
        Column {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "Your habits",
                fontSize = 24.sp
            )
            AllHabitList(habitViewModel)
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
        ) {
            FloatingActionButton(
                onClick = { dialogShown = true },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Localized description")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HabitListModulePreview() {
    HabitListModule()
}

