package com.grigorevmp.habits.domain.usecase.date

import com.grigorevmp.habits.data.data.DateEntity
import com.grigorevmp.habits.data.habit.HabitRefEntity
import com.grigorevmp.habits.data.repository.DateRepository
import com.grigorevmp.habits.data.repository.HabitRefRepository
import com.grigorevmp.habits.data.repository.HabitRepository
import com.grigorevmp.habits.data.repository.PreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

class UpdateSyncPointUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val habitRefRepository: HabitRefRepository,
    private val habitRepository: HabitRepository,
    private val dateRepository: DateRepository,
) {

    fun invoke() {
        CoroutineScope(Dispatchers.IO).launch {
            val lastDate = preferencesRepository.getLastSyncDate()
            val currentDate = LocalDate.now()

            var date = dateRepository.getDateId(currentDate)

            if (date == null) {
                dateRepository.insert(
                    DateEntity(date = currentDate)
                )
            }

            var endSync = false

            habitRepository.allHabits.collect { habits ->
                while (true) {

                    var date = dateRepository.getDateId(lastDate)

                    if (date == null) {
                        dateRepository.insert(
                            DateEntity(date = lastDate)
                        )

                        date = dateRepository.getDateId(lastDate)
                    }

                    for (habit in habits) {
                        val possibleHabit =
                            habitRepository.getHabitForDate(date?.id!!, habit.habit.id)

                        if (possibleHabit == null) {
                            habit.dateIdList.add(date.id!!)
                        }

                        habitRefRepository.insert(
                            HabitRefEntity(
                                dateId = date.id!!,
                                habitId = habit.habit.id,
                            )
                        )
                    }

                    if (endSync) break

                    lastDate.plusDays(1)

                    if (lastDate == currentDate) {
                        endSync = true
                    }
                }
            }

            preferencesRepository.updateLastSyncDate()
        }
    }
}