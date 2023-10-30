package com.grigorevmp.habits.domain.usecase.habits

import android.util.Log
import com.grigorevmp.habits.data.HabitEntity
import com.grigorevmp.habits.data.repository.HabitRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class UpdateHabitUseCase @Inject constructor(
    private var repository: HabitRepository,
) {

    fun invoke(
        habit: HabitEntity
    ) {
        Log.d("Habit updated", habit.toString())

        CoroutineScope(Dispatchers.IO).launch {
            repository.update(habit)
        }
    }
}