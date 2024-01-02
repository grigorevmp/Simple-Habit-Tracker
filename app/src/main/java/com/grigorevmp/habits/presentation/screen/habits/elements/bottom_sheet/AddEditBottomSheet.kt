package com.grigorevmp.habits.presentation.screen.habits.elements.bottom_sheet

import android.content.Context
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.grigorevmp.habits.R
import com.grigorevmp.habits.data.CountableEntity
import com.grigorevmp.habits.data.HabitCategory
import com.grigorevmp.habits.data.HabitEntity
import com.grigorevmp.habits.data.SerializableTimePickerState
import kotlinx.coroutines.launch
import java.time.DayOfWeek


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AddEditBottomSheet(
    openBottomSheetState: Boolean,
    bottomSheetState: SheetState,
    habitEntity: HabitEntity? = null,
    updateHabitEntity: (Context, HabitEntity) -> Unit = { _, _ -> },
    deleteHabitEntity: (HabitEntity) -> Unit = { _ -> },
    addHabitEntity: (Context, String, String, Array<DayOfWeek>, Boolean, SerializableTimePickerState, Boolean, CountableEntity?, HabitCategory) -> Unit = { _, _, _, _, _, _, _, _, _ -> },
    close: (Boolean) -> Unit,
) {
    val scope = rememberCoroutineScope()
    var showConfirmationDialog by remember { mutableStateOf(false) }

    if (openBottomSheetState) {
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch {
                    showConfirmationDialog = true
                }
           },
            sheetState = bottomSheetState,
        ) {
            AddEditHabitComponentWrapper(
                habitEntity = habitEntity,
                updateHabitEntity = updateHabitEntity,
                deleteHabitEntity = deleteHabitEntity,
                addHabitEntity = addHabitEntity,
            ) {
                close(false)
            }
        }
    }

    if (showConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text(stringResource(R.string.add_edit_bottom_sheet_cancel_changes)) },
            text = { Text(stringResource(R.string.add_edit_bottom_sheet_summary)) },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmationDialog = false
                        close(false)
                    }
                ) {
                    Text(stringResource(R.string.yes_button))
                }
            },
            dismissButton = {
                Button(onClick = {
                    showConfirmationDialog = false
                    close(true)
                    scope.launch {
                        bottomSheetState.show()
                    }
                }) {
                    Text(stringResource(R.string.no_button))
                }
            }
        )
    }
}