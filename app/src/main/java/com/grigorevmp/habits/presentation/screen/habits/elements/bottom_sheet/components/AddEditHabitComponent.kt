package com.grigorevmp.habits.presentation.screen.habits.elements.bottom_sheet.components

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grigorevmp.habits.R
import com.grigorevmp.habits.data.CountableEntity
import com.grigorevmp.habits.data.HabitCategory
import com.grigorevmp.habits.data.HabitCategory.BadHabits
import com.grigorevmp.habits.data.HabitCategory.Budget
import com.grigorevmp.habits.data.HabitCategory.Cleaning
import com.grigorevmp.habits.data.HabitCategory.Communication
import com.grigorevmp.habits.data.HabitCategory.Cooking
import com.grigorevmp.habits.data.HabitCategory.Diary
import com.grigorevmp.habits.data.HabitCategory.Education
import com.grigorevmp.habits.data.HabitCategory.FinanceControl
import com.grigorevmp.habits.data.HabitCategory.Food
import com.grigorevmp.habits.data.HabitCategory.FriendsTime
import com.grigorevmp.habits.data.HabitCategory.Hobby
import com.grigorevmp.habits.data.HabitCategory.Languages
import com.grigorevmp.habits.data.HabitCategory.Meditation
import com.grigorevmp.habits.data.HabitCategory.None
import com.grigorevmp.habits.data.HabitCategory.Personal
import com.grigorevmp.habits.data.HabitCategory.PetTime
import com.grigorevmp.habits.data.HabitCategory.PhysicalActivity
import com.grigorevmp.habits.data.HabitCategory.Planning
import com.grigorevmp.habits.data.HabitCategory.Productivity
import com.grigorevmp.habits.data.HabitCategory.Reading
import com.grigorevmp.habits.data.HabitCategory.Relaxation
import com.grigorevmp.habits.data.HabitCategory.SelfTime
import com.grigorevmp.habits.data.HabitCategory.Skills
import com.grigorevmp.habits.data.HabitCategory.StressFighting
import com.grigorevmp.habits.data.HabitCategory.Volunteering
import com.grigorevmp.habits.data.HabitCategory.WorkBalance
import com.grigorevmp.habits.data.HabitCategory.Working
import com.grigorevmp.habits.data.HabitEntity
import com.grigorevmp.habits.data.SerializableTimePickerState
import java.time.DayOfWeek

@ExperimentalMaterial3Api
@Composable
fun AddEditHabitComponent(
    habitEntity: HabitEntity? = null,
    updateHabitEntity: (Context, HabitEntity) -> Unit = { _, _ -> },
    deleteHabitEntity: (HabitEntity) -> Unit = { _ -> },
    addHabitEntity: (Context, String, String, Array<DayOfWeek>, Boolean, SerializableTimePickerState, Boolean, CountableEntity?, HabitCategory) -> Unit = { _, _, _, _, _, _, _, _, _ -> },
    hideDialog: () -> Unit,
) {
    val context = LocalContext.current

    val addHabit = habitEntity == null

    var title by remember { mutableStateOf(habitEntity?.title ?: "") }
    var description by remember { mutableStateOf(habitEntity?.description ?: "") }
    var selectedDays by remember {
        mutableStateOf(
            habitEntity?.days ?: DayOfWeek.entries.toTypedArray()
        )
    }
    var useAlert by remember { mutableStateOf(habitEntity?.alertEnabled ?: false) }
    var countableEntity by remember { mutableStateOf(habitEntity?.countableEntity) }
    var countableShown by remember { mutableStateOf(habitEntity?.countable ?: false) }
    var timePickerState by remember {
        mutableStateOf(
            SerializableTimePickerState(
                habitEntity?.time?.hour ?: 0,
                habitEntity?.time?.minute ?: 0,
            )
        )
    }

    val timeState = rememberTimePickerState(
        initialHour = timePickerState.hour, initialMinute = timePickerState.minute
    )

    var isError by rememberSaveable { mutableStateOf(false) }

    var habitCategory by remember {
        mutableStateOf(
            habitEntity?.habitCategory?.name ?: None.name
        )
    }

    fun validate(text: String): Boolean {
        isError = text.isBlank()
        return !isError
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp),
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = if (addHabit) stringResource(R.string.habit_screen_add_new_habit_dialog_title) else stringResource(
                R.string.habit_screen_edit_habit_dialog_title
            ),
            fontSize = 24.sp
        )

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            shape = RoundedCornerShape(8.dp),
            value = title,
            isError = isError,
            supportingText = {
                if (isError) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.habit_screen_edit_habit_enter_something_hint),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            trailingIcon = {
                if (isError) Icon(
                    Icons.Filled.Info,
                    stringResource(R.string.habit_screen_edit_habit_error_icon_description),
                    tint = MaterialTheme.colorScheme.error
                )
            },
            onValueChange = { newValue -> title = newValue },
            label = { Text(stringResource(R.string.habit_screen_edit_habit_title)) },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
            ),
        )

        TextField(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            value = description,
            onValueChange = { newValue -> description = newValue },
            label = { Text(stringResource(R.string.habit_screen_edit_habit_description)) },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
            ),
        )

        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = stringResource(R.string.habit_screen_edit_habit_edit_days_button),
            fontSize = 18.sp
        )

        WeekDaysFlowComponent(
            daysForHabit = selectedDays,
            modifier = Modifier.padding(top = 12.dp),
        ) { listWithDays ->
            selectedDays = listWithDays.toTypedArray()
        }

        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = stringResource(R.string.habit_property_category),
            fontSize = 18.sp
        )

        HabitCategoryDropdownMenu(habitCategory) {
            habitCategory = it.name
        }

        Row(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
        ) {

            Text(
                modifier = Modifier.align(CenterVertically),
                text = stringResource(R.string.habit_screen_edit_habit_change_notification_button),
                fontSize = 18.sp
            )

            Spacer(Modifier.weight(1f))

            Switch(
                checked = useAlert,
                onCheckedChange = { state -> useAlert = state },
                modifier = Modifier
                    .align(CenterVertically)
            )
        }

        val timeInputContentDescription = stringResource(R.string.time_input_content_description)

        AnimatedVisibility(
            visible = useAlert,
            enter = fadeIn(
                animationSpec = TweenSpec(200, 0, FastOutSlowInEasing)
            ),
            exit = fadeOut(
                animationSpec = TweenSpec(200, 0, FastOutLinearInEasing)
            ),
        ) {
            TimeInput(
                state = timeState,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .align(Alignment.CenterHorizontally)
                    .semantics {
                        this.contentDescription = timeInputContentDescription
                    },
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {

            Text(
                modifier = Modifier.align(CenterVertically),
                text = stringResource(R.string.habit_screen_add_new_habit_dialog_countable_change_button),
                fontSize = 18.sp
            )

            Spacer(Modifier.weight(1f))

            Switch(
                checked = countableShown,
                onCheckedChange = { state -> countableShown = state },
                modifier = Modifier
                    .align(CenterVertically)
            )
        }

        AnimatedVisibility(
            visible = countableShown,
            enter = fadeIn(
                animationSpec = TweenSpec(200, 0, FastOutSlowInEasing)
            ),
            exit = fadeOut(
                animationSpec = TweenSpec(200, 0, FastOutLinearInEasing)
            ),
        ) {
            CountableValueComponent(
                countableEntity = countableEntity
            ) { newCountableEntity: CountableEntity? ->
                countableEntity = newCountableEntity
            }
        }

        if (addHabit) {
            Button(modifier = Modifier
                .align(Alignment.End)
                .padding(vertical = 16.dp), onClick = {
                if (validate(title)) {
                    hideDialog()
                    addHabitEntity(
                        context,
                        title,
                        description,
                        selectedDays,
                        useAlert,
                        SerializableTimePickerState(
                            hour = timeState.hour,
                            minute = timeState.minute,
                        ),
                        countableEntity != null,
                        countableEntity,
                        HabitCategory.valueOf(habitCategory),
                    )
                }
            }) {
                Text(stringResource(R.string.habit_screen_add_new_habit_add_habit_button))
            }
        } else {
            habitEntity?.also {
                Row(
                    modifier = Modifier.padding(vertical = 16.dp)
                ) {
                    Button(modifier = Modifier
                        .padding(vertical = 16.dp)
                        .padding(bottom = 16.dp),
                        onClick = {
                            deleteHabitEntity(habitEntity)
                            hideDialog()
                        }) {
                        Text(stringResource(R.string.habit_screen_edit_habit_delete_button))
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Button(modifier = Modifier.padding(vertical = 16.dp), onClick = {

                        timePickerState =
                            SerializableTimePickerState(timeState.hour, timeState.minute)

                        if (validate(title)) {
                            habitEntity.title = title
                            habitEntity.description = description
                            habitEntity.days = selectedDays
                            habitEntity.alertEnabled = useAlert
                            habitEntity.time = SerializableTimePickerState(
                                hour = timePickerState.hour,
                                minute = timePickerState.minute,
                            )
                            habitEntity.countableEntity = countableEntity
                            habitEntity.countable = countableEntity != null
                            habitEntity.habitCategory = HabitCategory.valueOf(habitCategory)

                            updateHabitEntity(context, habitEntity)
                            hideDialog()
                        }
                    }) {
                        Text(stringResource(R.string.habit_screen_edit_habit_save_changes_button))
                    }
                }
            }
        }
    }
}

@Composable
fun HabitCategoryDropdownMenu(
    selectedCategory: String,
    onCategorySelected: (HabitCategory) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val items = HabitCategory.entries.toList()
    val selectedIndex =
        remember { mutableIntStateOf(items.indexOf(HabitCategory.valueOf(selectedCategory))) }

    val selectedCategoryText = getHabitNameValue(items[selectedIndex.intValue].name)

    Column(
        modifier = Modifier.padding(top = 12.dp)
    ) {
        Button(onClick = { expanded = true }) {
            Text(text = selectedCategoryText)
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "Dropdown"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            items.forEachIndexed { index, category ->
                DropdownMenuItem(
                    text = { Text(getHabitNameValue(category.name)) },
                    onClick = {
                        selectedIndex.intValue = index
                        onCategorySelected(HabitCategory.valueOf(category.name))
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun getHabitNameValue(
    habitName: String
) = when (HabitCategory.valueOf(habitName)) {
    None -> stringResource(R.string.habit_property_category_none)
    Food -> stringResource(R.string.habit_property_category_food)
    PhysicalActivity -> stringResource(R.string.habit_property_category_sport)
    Relaxation -> stringResource(R.string.habit_property_category_relax)
    Meditation -> stringResource(R.string.habit_property_category_meditation)
    BadHabits -> stringResource(R.string.habit_property_category_bad)
    Reading -> stringResource(R.string.habit_property_category_reading)
    Education -> stringResource(R.string.habit_property_category_education)
    Languages -> stringResource(R.string.habit_property_category_lang)
    Skills -> stringResource(R.string.habit_property_category_skills)
    Planning -> stringResource(R.string.habit_property_category_planning)
    Working -> stringResource(R.string.habit_property_category_working)
    Diary -> stringResource(R.string.habit_property_category_diary)
    StressFighting -> stringResource(R.string.habit_property_category_tress)
    Communication -> stringResource(R.string.habit_property_category_comm)
    SelfTime -> stringResource(R.string.habit_property_category_self)
    Productivity -> stringResource(R.string.habit_property_category_productivity)
    WorkBalance -> stringResource(R.string.habit_property_category_work_life_balance)
    FinanceControl -> stringResource(R.string.habit_property_category_finance_control)
    Budget -> stringResource(R.string.habit_property_category_budget)
    Hobby -> stringResource(R.string.habit_property_category_hobby)
    Cleaning -> stringResource(R.string.habit_property_category_cleaning)
    Cooking -> stringResource(R.string.habit_property_category__cooking)
    PetTime -> stringResource(R.string.habit_property_category_pet_time)
    Personal -> stringResource(R.string.habit_property_category_personal)
    Volunteering -> stringResource(R.string.habit_property_category_volunteering)
    FriendsTime -> stringResource(R.string.habit_property_category_friends)
}


@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
fun EditHabitDialogPreview() {
    AddEditHabitComponent(habitEntity = HabitEntity(
        id = 0,
        description = "bla-bla",
        title = "Test habit",
        alertEnabled = true,
        time = SerializableTimePickerState(10, 10),
        days = arrayOf(DayOfWeek.MONDAY),
        completed = false,
        deleted = false,
        countable = true,
    ),
        updateHabitEntity = { _, _ -> },
        deleteHabitEntity = { },
        addHabitEntity = { _, _, _, _, _, _, _, _, _ -> },
        hideDialog = { })
}