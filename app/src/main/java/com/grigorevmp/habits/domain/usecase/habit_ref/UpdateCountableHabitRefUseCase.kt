package com.grigorevmp.habits.domain.usecase.habit_ref

import com.grigorevmp.habits.data.habit.HabitType
import com.grigorevmp.habits.data.repository.HabitRefRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class UpdateCountableHabitRefUseCase @Inject constructor(
    private var repository: HabitRefRepository,
) {

    fun invoke(dateId: Long, habitId: Long, habitType: HabitType, value: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.update(dateId, habitId, habitType, value)
        }
    }
}