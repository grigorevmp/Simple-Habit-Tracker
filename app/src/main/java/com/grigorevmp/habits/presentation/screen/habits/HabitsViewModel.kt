package com.grigorevmp.habits.presentation.screen.habits

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grigorevmp.habits.data.HabitEntity
import com.grigorevmp.habits.data.SerializableTimePickerState
import com.grigorevmp.habits.domain.usecase.synchronizer.AddSyncPointForNewHabitUseCase
import com.grigorevmp.habits.domain.usecase.habits.AddHabitUseCase
import com.grigorevmp.habits.domain.usecase.habits.DeleteHabitUseCase
import com.grigorevmp.habits.domain.usecase.habits.GetOnlyHabitsUseCase
import com.grigorevmp.habits.domain.usecase.habits.UpdateHabitUseCase
import com.grigorevmp.habits.core.alarm.AlarmScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import javax.inject.Inject


@HiltViewModel
class HabitsViewModel @Inject constructor(
    getHabitsUseCase: GetOnlyHabitsUseCase,
    private val addHabitUseCase: AddHabitUseCase,
    private val deleteHabitUseCase: DeleteHabitUseCase,
    private val updateHabitUseCase: UpdateHabitUseCase,
    private val addSyncPointForNewHabitUseCase: AddSyncPointForNewHabitUseCase,
) : ViewModel() {

    private val alarmScheduler = AlarmScheduler()

    var habits = getHabitsUseCase.invoke().stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), emptyList())

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

    fun deleteHabit(habit: HabitEntity) = viewModelScope.launch {
        deleteHabitUseCase.invoke(habit)
    }
}