package com.grigorevmp.habits.presentation.screen.habits

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grigorevmp.habits.data.CountableEntity
import com.grigorevmp.habits.data.HabitEntity
import com.grigorevmp.habits.data.SerializableTimePickerState
import com.grigorevmp.habits.data.habit.HabitType
import com.grigorevmp.habits.domain.usecase.date.GetDateIdUseCase
import com.grigorevmp.habits.domain.usecase.habit_ref.GetAllDateOfHabitRefUseCase
import com.grigorevmp.habits.domain.usecase.habits.AddHabitUseCase
import com.grigorevmp.habits.domain.usecase.habits.DeleteHabitUseCase
import com.grigorevmp.habits.domain.usecase.habits.GetOnlyHabitsUseCase
import com.grigorevmp.habits.domain.usecase.habits.UpdateHabitUseCase
import com.grigorevmp.habits.domain.usecase.scheduler.ScheduleAlarmUseCase
import com.grigorevmp.habits.domain.usecase.synchronizer.AddSyncPointForNewHabitUseCase
import com.grigorevmp.habits.presentation.screen.habits.data.StatDay
import com.grigorevmp.habits.presentation.screen.habits.data.StatMonth
import com.grigorevmp.habits.presentation.screen.habits.data.StatYear
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import javax.inject.Inject


@HiltViewModel
class HabitsViewModel @Inject constructor(
    private val scheduleAlarmUseCase: ScheduleAlarmUseCase,
    private val getHabitsUseCase: GetOnlyHabitsUseCase,
    private val addHabitUseCase: AddHabitUseCase,
    private val getDateIdUseCase: GetDateIdUseCase,
    private val deleteHabitUseCase: DeleteHabitUseCase,
    private val updateHabitUseCase: UpdateHabitUseCase,
    private val addSyncPointForNewHabitUseCase: AddSyncPointForNewHabitUseCase,
    private val getAllDateOfHabitRefUseCase: GetAllDateOfHabitRefUseCase,
) : ViewModel() {

    var uiState = HabitListScreenUiState(
        habitsData = getHabitsUseCase.invoke()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), emptyList())
    )
        private set

    fun loadData(payload: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val resultMap = mutableMapOf<Long, List<StatYear>>()

            getHabitsUseCase.invoke().collectLatest { habits ->
                habits.forEach { habit ->
                    getAllDateOfHabitRefUseCase.invoke(habit.id).first { habitList ->
                        val all = habitList.sortedBy { it.dateId }
                            .filter {
                                val date = getDateIdUseCase.invoke(it.dateId)?.date
                                date != null
                            }.map {
                                val date = getDateIdUseCase.invoke(it.dateId)?.date!!
                                date to (it.habitType == HabitType.Done)
                            }

                        val allYears = all.groupBy { it.first.year }.map { habitsByYear ->
                            val year = habitsByYear.key
                            val months =
                                habitsByYear.value.groupBy { it.first.month }.map { habitByMonth ->
                                    val month = habitByMonth.key
                                    val sumOfCompletedHabits =
                                        habitByMonth.value.map { if (it.second) 1 else 0 }.sum()
                                    val allHabits = habitByMonth.value.size
                                    val percentOfMonth =
                                        if (allHabits > 0) sumOfCompletedHabits.toFloat() / allHabits * 100 else 0f

                                    val days = habitByMonth.value.map {
                                        StatDay(it.first.dayOfMonth, if (it.second) 1f else 0f)
                                    }

                                    StatMonth(
                                        index = month.ordinal,
                                        percent = percentOfMonth,
                                        days = days,
                                    )
                                }

                            StatYear(
                                year = year,
                                months = months,
                            )
                        }

                        resultMap[habit.id] = allYears
                        true
                    }
                }
                uiState.statisticData.value = resultMap
                payload()
            }
            cancel()
        }
    }


    fun loadByDates(habitId: Long) = flow {
        getAllDateOfHabitRefUseCase.invoke(habitId).collect { habitList ->

            val all = habitList.sortedBy { it.dateId }
                .filter {
                    val date = getDateIdUseCase.invoke(it.dateId)?.date
                    date != null
                }.map {
                    val date = getDateIdUseCase.invoke(it.dateId)?.date!!
                    date to (it.habitType == HabitType.Done)
                }

            val allYears = all.groupBy { it.first.year }.map { habitsByYear ->
                val year = habitsByYear.key
                val months = habitsByYear.value.groupBy { it.first.month }.map { habitByMonth ->
                    val month = habitByMonth.key
                    val sumOfCompletedHabits =
                        habitByMonth.value.map { if (it.second) 1 else 0 }.sum()
                    val allHabits = habitByMonth.value.size
                    val percentOfMonth =
                        if (allHabits > 0) sumOfCompletedHabits.toFloat() / allHabits * 100 else 0f

                    val days = habitByMonth.value.map {
                        StatDay(it.first.dayOfMonth, if (it.second) 1f else 0f)
                    }

                    StatMonth(
                        index = month.ordinal,
                        percent = percentOfMonth,
                        days = days,
                    )
                }

                StatYear(
                    year = year,
                    months = months,
                )
            }

            emit(allYears)
        }
    }.flowOn(Dispatchers.IO)

    fun addHabit(
        context: Context,
        title: String,
        description: String,
        selectedDays: Array<DayOfWeek>,
        useAlert: Boolean,
        timePickerState: SerializableTimePickerState,
        countable: Boolean,
        countableEntity: CountableEntity?,
    ) = viewModelScope.launch {
        CoroutineScope(Dispatchers.IO).launch {
            val habit = addHabitUseCase.invoke(
                title,
                description,
                selectedDays,
                useAlert,
                timePickerState,
                countable,
                countableEntity,
            )

            if (useAlert) {
                scheduleAlarmUseCase.invoke(context, habit)
            }

            addSyncPointForNewHabitUseCase.invoke(habit.id)
        }
    }

    fun updateHabit(context: Context, habit: HabitEntity) = viewModelScope.launch {
        updateHabitUseCase.invoke(habit)

        if (habit.alertEnabled) {
            scheduleAlarmUseCase.invoke(context, habit)
        }
    }

    fun deleteHabit(habit: HabitEntity) = viewModelScope.launch {
        deleteHabitUseCase.invoke(habit)
    }
}