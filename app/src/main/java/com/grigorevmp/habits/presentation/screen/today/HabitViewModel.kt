package com.grigorevmp.habits.presentation.screen.today

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grigorevmp.habits.data.HabitEntity
import com.grigorevmp.habits.data.SerializableTimePickerState
import com.grigorevmp.habits.data.data.DateEntity
import com.grigorevmp.habits.data.habit.HabitType
import com.grigorevmp.habits.domain.usecase.date.GetDateUseCase
import com.grigorevmp.habits.domain.usecase.date_synchronizer.AddSyncPointForNewHabitUseCase
import com.grigorevmp.habits.domain.usecase.habit_ref.GetHabitRefUseCase
import com.grigorevmp.habits.domain.usecase.habit_ref.UpdateHabitRefUseCase
import com.grigorevmp.habits.domain.usecase.habits.AddHabitUseCase
import com.grigorevmp.habits.domain.usecase.habits.DeleteHabitUseCase
import com.grigorevmp.habits.domain.usecase.habits.GetOnlyHabitsUseCase
import com.grigorevmp.habits.domain.usecase.habits.UpdateHabitUseCase
import com.grigorevmp.habits.core.alarm.AlarmScheduler
import com.grigorevmp.habits.presentation.screen.today.data.HabitEntityUI
import com.grigorevmp.habits.presentation.screen.today.data.HabitWithDatesUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject


@HiltViewModel
class HabitViewModel @Inject constructor(
    private val getHabitsUseCase: GetOnlyHabitsUseCase,
    private val addHabitUseCase: AddHabitUseCase,
    private val deleteHabitUseCase: DeleteHabitUseCase,
    private val updateHabitUseCase: UpdateHabitUseCase,
    private val getDateUseCase: GetDateUseCase,
    private val getHabitRefUseCase: GetHabitRefUseCase,
    private val updateHabitRefUseCase: UpdateHabitRefUseCase,
    private val addSyncPointForNewHabitUseCase: AddSyncPointForNewHabitUseCase,
) : ViewModel() {

    private val alarmScheduler = AlarmScheduler()

    var uiState = mutableStateOf<List<HabitWithDatesUI>>(mutableListOf())
        private set

    var habits = getHabitsUseCase.invoke().stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), emptyList())

    fun getPreparedHabitsList(daysCount: Int, payload: () -> Unit) = viewModelScope.launch {
        CoroutineScope(Dispatchers.IO).launch {
            getHabitsUseCase.invoke().collect { habitsData ->
                val minorAllHabitsWithDateData = mutableListOf<HabitWithDatesUI>()
                val datesData = getDatesCut(daysCount)

                for (date in datesData) {
                    getDate(date).collect { targetDate ->
                        val habitWithDate = HabitWithDatesUI(
                            date = "${date.dayOfMonth} ${
                                date.month.toString().lowercase()
                            } · ${date.dayOfWeek.toString().lowercase()}",
                            habits = arrayListOf()
                        )

                        targetDate.id?.let { targetDateId ->
                            for (habit in habitsData) {
                                if (habit.deleted) continue

                                val habitRef =
                                    getHabitRefUseCase.invoke(
                                        targetDateId,
                                        habit.id
                                    )

                                if (habitRef != null) {

                                    habitWithDate.habits.add(
                                        HabitEntityUI(
                                            id = habit.id,
                                            dateId = targetDateId,
                                            title = habit.title,
                                            description = habit.description,
                                            type = habitRef.habitType
                                        )
                                    )
                                }
                            }
                        }

                        minorAllHabitsWithDateData.add(habitWithDate)
                    }
                }

                uiState.value = minorAllHabitsWithDateData
                payload()
                cancel()
            }
        }
    }

    fun addHabit(
        context: Context,
        title: String,
        description: String,
        selectedDays: Array<DayOfWeek>,
        useAlert: Boolean,
        timePickerState: SerializableTimePickerState,
    ) = viewModelScope.launch {
        CoroutineScope(Dispatchers.IO).launch {
            val habit = addHabitUseCase.invoke(
                title,
                description,
                selectedDays,
                useAlert,
                timePickerState
            )

            if (useAlert) {
                alarmScheduler.schedule(context, habit)
            }

            addSyncPointForNewHabitUseCase.invoke(habit.id)
        }
    }

    fun updateHabit(context: Context, habit: HabitEntity) = viewModelScope.launch {
        updateHabitUseCase.invoke(habit)

        if (habit.alertEnabled) {
            alarmScheduler.schedule(context, habit)
        }
    }

    fun updateHabitRef(dateId: Long, habitId: Long, habitType: HabitType) = viewModelScope.launch {
        updateHabitRefUseCase.invoke(dateId, habitId, habitType)
    }

    fun deleteHabit(habit: HabitEntity) = viewModelScope.launch {
        deleteHabitUseCase.invoke(habit)
    }


    private fun getDate(date: LocalDate): Flow<DateEntity> = flow {
        getDateUseCase.invoke(date)?.also {
            emit(it)
        }
    }.flowOn(Dispatchers.IO)

    private fun getDatesCut(days: Int): List<LocalDate> {
        val dates = mutableListOf<LocalDate>()
        var date = LocalDate.now()

        for (i in 0..<days) {
            dates.add(date)
            date = date.minusDays(1)
        }
        return dates
    }
}