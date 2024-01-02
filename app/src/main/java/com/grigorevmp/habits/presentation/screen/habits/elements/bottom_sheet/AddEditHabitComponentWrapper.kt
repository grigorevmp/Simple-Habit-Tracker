package com.grigorevmp.habits.presentation.screen.habits.elements.bottom_sheet

import android.content.Context
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.grigorevmp.habits.data.CountableEntity
import com.grigorevmp.habits.data.HabitCategory
import com.grigorevmp.habits.data.HabitEntity
import com.grigorevmp.habits.data.SerializableTimePickerState
import com.grigorevmp.habits.presentation.screen.habits.elements.bottom_sheet.components.AddEditHabitComponent
import java.time.DayOfWeek

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditHabitComponentWrapper(
    habitEntity: HabitEntity? = null,
    updateHabitEntity: (Context, HabitEntity) -> Unit = { _, _ -> },
    deleteHabitEntity: (HabitEntity) -> Unit = { _ -> },
    addHabitEntity: (Context, String, String, Array<DayOfWeek>, Boolean, SerializableTimePickerState, Boolean, CountableEntity?, HabitCategory) -> Unit = { _, _, _, _, _, _, _, _, _ -> },
    hideDialog: () -> Unit,
) {
    AddEditHabitComponent(
        habitEntity = habitEntity,
        updateHabitEntity = updateHabitEntity,
        deleteHabitEntity = deleteHabitEntity,
        addHabitEntity = addHabitEntity,
        hideDialog = hideDialog
    )
}


@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
fun EditHabitBottomSheetPreview() {
    AddEditHabitComponentWrapper(
        habitEntity = HabitEntity(
            id = 0,
            description = "bla-bla",
            title = "Test habit",
            alertEnabled = true,
            time = SerializableTimePickerState(10, 10),
            days = arrayOf(DayOfWeek.MONDAY),
            completed = false,
            deleted = false,
        ),
        updateHabitEntity = { _, _ -> },
        deleteHabitEntity = { },
        hideDialog = { }
    )
}