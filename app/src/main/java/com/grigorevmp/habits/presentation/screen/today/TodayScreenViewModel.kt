package com.grigorevmp.habits.presentation.screen.today

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grigorevmp.habits.data.data.DateEntity
import com.grigorevmp.habits.data.habit.HabitType
import com.grigorevmp.habits.domain.usecase.date.GetDateUseCase
import com.grigorevmp.habits.domain.usecase.habit_ref.GetHabitRefUseCase
import com.grigorevmp.habits.domain.usecase.habit_ref.UpdateHabitRefUseCase
import com.grigorevmp.habits.domain.usecase.habits.GetOnlyHabitsUseCase
import com.grigorevmp.habits.presentation.screen.today.data.HabitEntityUI
import com.grigorevmp.habits.presentation.screen.today.data.HabitStatisticItemUi
import com.grigorevmp.habits.presentation.screen.today.data.HabitWithDatesUI
import com.grigorevmp.habits.presentation.screen.today.mapper.TodayScreenMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject


@HiltViewModel
class TodayScreenViewModel @Inject constructor(
    private val getHabitsUseCase: GetOnlyHabitsUseCase,
    private val getDateUseCase: GetDateUseCase,
    private val getHabitRefUseCase: GetHabitRefUseCase,
    private val updateHabitRefUseCase: UpdateHabitRefUseCase,
    private val todayScreenMapper: TodayScreenMapper,
) : ViewModel() {

    var uiState = mutableStateOf<List<HabitWithDatesUI>>(mutableListOf())
        private set

    var statisticUiState = MutableStateFlow(listOf<HabitStatisticItemUi>())
        private set

    var habits = getHabitsUseCase.invoke()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), emptyList())

    fun getPreparedHabitsList(context: Context, daysCount: Int, payload: () -> Unit) =
        viewModelScope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                getHabitsUseCase.invoke().collect { habitsData ->
                    val minorAllHabitsWithDateData = mutableListOf<HabitWithDatesUI>()
                    val datesData = getDatesCut(daysCount)

                    for (date in datesData) {
                        getDate(date).collect { targetDate ->
                            val habitWithDate = HabitWithDatesUI(
                                date = todayScreenMapper.getFancyTodayString(
                                    context,
                                    date.dayOfMonth,
                                    date.month,
                                    date.dayOfWeek
                                ),
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

    fun updateWeekStatistic() {
        CoroutineScope(Dispatchers.IO).launch {
            getHabitsUseCase.invoke().collectLatest { habitsData ->
                val minorAllHabitStatisticItemUiData = mutableListOf<HabitStatisticItemUi>()
                val datesData = getWeekDays()

                for ((index, date) in datesData.withIndex()) {
                    val statisticItem = HabitStatisticItemUi(0, 0, index)

                    getDateOrNull(date).collect { targetDate ->
                        if (targetDate == null) return@collect

                        for (habit in habitsData) {

                            if (habit.deleted) continue

                            val habitRef = getHabitRefUseCase.invoke(
                                targetDate.id!!,
                                habit.id
                            )

                            if (habitRef != null) {
                                statisticItem.habitsNumber += 1

                                if (habitRef.habitType == HabitType.Done) {
                                    statisticItem.checkedHabitsNumber += 1
                                }
                            }
                        }
                    }

                    minorAllHabitStatisticItemUiData.add(statisticItem)
                }

                statisticUiState.value = minorAllHabitStatisticItemUiData
            }
            cancel()
        }
    }

    fun updateHabitRef(dateId: Long, habitId: Long, habitType: HabitType) = viewModelScope.launch {
        updateHabitRefUseCase.invoke(dateId, habitId, habitType)
        updateWeekStatistic()
    }

    private fun getDate(date: LocalDate): Flow<DateEntity> = flow {
        getDateUseCase.invoke(date)?.also {
            emit(it)
        }
    }.flowOn(Dispatchers.IO)


    private fun getDateOrNull(date: LocalDate): Flow<DateEntity?> = flow {
        emit(getDateUseCase.invoke(date))
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

    private fun getWeekDays(): Array<LocalDate> {
        val startDate = LocalDate.now().with(java.time.DayOfWeek.MONDAY)
        return Array(7) { i -> startDate.plusDays(i.toLong()) }
    }
}