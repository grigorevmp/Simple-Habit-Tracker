package com.grigorevmp.habits.domain.usecase.habits

import com.grigorevmp.habits.data.repository.HabitRepository
import javax.inject.Inject

class GetOnlyHabitsUseCase @Inject constructor(
    private var repository: HabitRepository,
) {

    fun invoke() = repository.fetchOnlyHabits()
}