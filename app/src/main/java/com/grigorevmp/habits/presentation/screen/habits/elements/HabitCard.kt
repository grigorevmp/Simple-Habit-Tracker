package com.grigorevmp.habits.presentation.screen.habits.elements

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grigorevmp.habits.R
import com.grigorevmp.habits.core.utils.Utils
import com.grigorevmp.habits.data.CountableEntity
import com.grigorevmp.habits.data.HabitCategory
import com.grigorevmp.habits.data.HabitEntity
import com.grigorevmp.habits.data.SerializableTimePickerState
import com.grigorevmp.habits.presentation.screen.habits.data.StatYear
import com.grigorevmp.habits.presentation.screen.habits.elements.bottom_sheet.AddEditBottomSheet
import com.grigorevmp.habits.presentation.screen.habits.elements.common.ActionIcon
import com.grigorevmp.habits.presentation.screen.habits.elements.stastistic.MonthCard
import com.grigorevmp.habits.presentation.screen.habits.elements.stastistic.YearCard
import com.grigorevmp.habits.presentation.screen.habits.parseToDate
import com.grigorevmp.habits.presentation.screen.habits.toReadableForms
import java.time.DayOfWeek

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitCard(
    habitEntity: HabitEntity,
    updateHabitEntity: (Context, HabitEntity) -> Unit,
    deleteHabitEntity: (HabitEntity) -> Unit,
    getAllHabitDates: Map<Long, List<StatYear>>,
) {
    val title = habitEntity.title
    val description = habitEntity.description
    val alertEnabled = habitEntity.alertEnabled
    val category = habitEntity.habitCategory
    val countable = habitEntity.countable
    val countableEntity = habitEntity.countableEntity
    val days = habitEntity.days.parseToDate(LocalContext.current)
    val time = habitEntity.time.toReadableForms()

    var selectedHabit by remember { mutableStateOf<HabitEntity?>(null) }

    val isStatisticsOpened = remember { mutableStateOf(false) }
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    selectedHabit?.also {
        AddEditBottomSheet(
            openBottomSheet,
            bottomSheetState,
            habitEntity = it,
            updateHabitEntity = updateHabitEntity,
            deleteHabitEntity = deleteHabitEntity,
        ) { state ->
            openBottomSheet = state
        }
    }

    Card(modifier = Modifier
        .padding(vertical = 4.dp, horizontal = 8.dp)
        .fillMaxWidth(), onClick = {
        if (!isStatisticsOpened.value) {
            selectedHabit = habitEntity
            openBottomSheet = true
        } else {
            isStatisticsOpened.value = !isStatisticsOpened.value
        }
    }) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Row(
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Column(
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Text(
                        text = title,
                        fontSize = 20.sp,
                    )

                    if (description.isNotBlank()) {
                        Text(text = description, modifier = Modifier.padding(top = 4.dp))
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                AnimatedVisibility(
                    visible = isStatisticsOpened.value,
                    enter = scaleIn(
                        animationSpec = TweenSpec(200, 0, FastOutSlowInEasing)
                    ),
                    exit = scaleOut(
                        animationSpec = TweenSpec(200, 0, FastOutLinearInEasing)
                    ),
                ) {
                    ActionIcon(
                        iconId = R.drawable.ic_edit,
                        iconDescription = stringResource(R.string.edit_icon_description)
                    ) {
                        selectedHabit = habitEntity
                        openBottomSheet = true
                    }
                }

                ActionIcon(
                    iconId = R.drawable.ic_stat,
                    iconDescription = stringResource(R.string.statistic_icon_description)
                ) {
                    isStatisticsOpened.value = !isStatisticsOpened.value
                }
            }

            if (category != HabitCategory.None) {
                CategoryCard(category.name)
            }

            DateInfoCard(days)

            if (countable && countableEntity != null) {
                CountableInfoCard(countableEntity)
            }

            if (alertEnabled) {
                NotificationInfoCard(time, habitEntity)
            }

            AnimatedVisibility(
                visible = isStatisticsOpened.value, enter = fadeIn(
                    animationSpec = TweenSpec(400, 0, FastOutSlowInEasing)
                ) + expandVertically(
                    animationSpec = TweenSpec(200, 0, FastOutSlowInEasing)
                )
            ) {
                TabCard(
                    habitEntity.id,
                    getAllHabitDates,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabCard(
    habitId: Long,
    getAllHabitDates: Map<Long, List<StatYear>>,
) {
    val tabOptions = listOf(
        stringResource(R.string.statistic_month),
        stringResource(R.string.statistic_year),
    )
    var selectedTab by remember { mutableIntStateOf(0) }
    val stats = getAllHabitDates[habitId]

    Card(
        Modifier.padding(top = 8.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            PrimaryTabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.surfaceBright,
                indicator = @Composable {
                    TabRowDefaults.PrimaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(
                            selectedTab,
                            matchContentSize = true
                        ),
                        width = Dp.Unspecified,
                    )
                },
            ) {
                tabOptions.forEachIndexed { index, option ->
                    Tab(text = { Text(option) },
                        selected = selectedTab == index,
                        onClick = { selectedTab = index })
                }
            }

            AnimatedVisibility(
                visible = selectedTab == 1,
            ) {
                YearCard(stats)
            }

            AnimatedVisibility(
                visible = selectedTab != 1,
            ) {
                MonthCard(stats)
            }
        }
    }
}

@Composable
private fun DateInfoCard(days: String) {
    Card(
        modifier = Modifier.padding(top = 8.dp), colors = CardDefaults.cardColors(
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
                contentDescription = stringResource(R.string.reminder_icon_description),

                modifier = Modifier.align(Alignment.CenterVertically)
            )

            Text(
                text = days,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 8.dp),
            )
        }
    }
}

@Composable
private fun CountableInfoCard(countableEntity: CountableEntity) {
    Card(
        modifier = Modifier.padding(top = 8.dp), colors = CardDefaults.cardColors(
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
                Icons.Outlined.Info,
                contentDescription = stringResource(R.string.reminder_icon_description),

                modifier = Modifier.align(Alignment.CenterVertically)
            )

            Text(
                text = "${countableEntity.actionName} ${countableEntity.targetValue} ${countableEntity.valueName}",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 8.dp),
            )
        }
    }
}

@Composable
private fun CategoryCard(category: String) {
    Card(
        modifier = Modifier.padding(top = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary
        )
    ) {
        val selectedCategoryText = when(HabitCategory.valueOf(category)){
            HabitCategory.None -> stringResource(R.string.habit_property_category_none)
            HabitCategory.Food -> stringResource(R.string.habit_property_category_food)
            HabitCategory.PhysicalActivity -> stringResource(R.string.habit_property_category_sport)
            HabitCategory.Relaxation -> stringResource(R.string.habit_property_category_relax)
            HabitCategory.Meditation -> stringResource(R.string.habit_property_category_meditation)
            HabitCategory.BadHabits -> stringResource(R.string.habit_property_category_bad)
            HabitCategory.Reading -> stringResource(R.string.habit_property_category_reading)
            HabitCategory.Education -> stringResource(R.string.habit_property_category_education)
            HabitCategory.Languages -> stringResource(R.string.habit_property_category_lang)
            HabitCategory.Skills -> stringResource(R.string.habit_property_category_skills)
            HabitCategory.Planning -> stringResource(R.string.habit_property_category_planning)
            HabitCategory.Working -> stringResource(R.string.habit_property_category_working)
            HabitCategory.Diary -> stringResource(R.string.habit_property_category_diary)
            HabitCategory.StressFighting -> stringResource(R.string.habit_property_category_tress)
            HabitCategory.Communication -> stringResource(R.string.habit_property_category_comm)
            HabitCategory.SelfTime -> stringResource(R.string.habit_property_category_self)
            HabitCategory.Productivity -> stringResource(R.string.habit_property_category_productivity)
            HabitCategory.WorkBalance -> stringResource(R.string.habit_property_category_work_life_balance)
            HabitCategory.FinanceControl -> stringResource(R.string.habit_property_category_finance_control)
            HabitCategory.Budget -> stringResource(R.string.habit_property_category_budget)
            HabitCategory.Hobby -> stringResource(R.string.habit_property_category_hobby)
            HabitCategory.Cleaning -> stringResource(R.string.habit_property_category_cleaning)
            HabitCategory.Cooking -> stringResource(R.string.habit_property_category__cooking)
            HabitCategory.PetTime -> stringResource(R.string.habit_property_category_pet_time)
            HabitCategory.Personal -> stringResource(R.string.habit_property_category_personal)
            HabitCategory.Volunteering -> stringResource(R.string.habit_property_category_volunteering)
            HabitCategory.FriendsTime -> stringResource(R.string.habit_property_category_friends)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painterResource(id = R.drawable.ic_category),
                contentDescription = stringResource(R.string.reminder_icon_description),
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            Text(
                text = selectedCategoryText,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 8.dp),
            )
        }
    }
}

@Composable
private fun NotificationInfoCard(time: String, habitEntity: HabitEntity) {
    Card(
        modifier = Modifier.padding(top = 8.dp), colors = CardDefaults.cardColors(
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
                Icons.Outlined.Notifications,
                contentDescription = stringResource(R.string.reminder_icon_description),

                modifier = Modifier.align(Alignment.CenterVertically)
            )

            Text(
                text = stringResource(R.string.habit_screen_remind_at, time),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 8.dp),
            )

            Spacer(modifier = Modifier.weight(1f))


            val requestCode = (habitEntity.id * 100).toInt() + habitEntity.days[0].ordinal

            if (!Utils.checkIfPendingIntentIsRegistered(
                    LocalContext.current, requestCode
                )
            ) {
                Icon(
                    Icons.Filled.Warning,
                    contentDescription = stringResource(R.string.failed_icon_description),

                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HabitCardPreview() {
    HabitCard(
        habitEntity = HabitEntity(
            id = 0,
            title = "Title",
            description = "Description",
            days = arrayOf(DayOfWeek.FRIDAY),
            alertEnabled = true,
            time = SerializableTimePickerState(10, 10),
            completed = false,
            countable = true,
            countableEntity = CountableEntity(
                30, "Bla-bla", "Do"
            )
        ),
        updateHabitEntity = { _, _ -> },
        deleteHabitEntity = { _ -> },
        getAllHabitDates = mapOf(),
    )
}

@Preview
@Composable
fun TabCardPreview() {
    TabCard(
        habitId = 0L,
        getAllHabitDates = mapOf()
    )
}