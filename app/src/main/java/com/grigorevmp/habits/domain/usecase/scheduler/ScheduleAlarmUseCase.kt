package com.grigorevmp.habits.domain.usecase.scheduler

import android.content.Context
import com.grigorevmp.habits.core.alarm.AlarmScheduler
import com.grigorevmp.habits.data.HabitEntity
import javax.inject.Inject

class ScheduleAlarmUseCase @Inject constructor() {
    private val alarmScheduler = AlarmScheduler()

    fun invoke(context: Context, item: HabitEntity) {
        alarmScheduler.schedule(context, item)
    }
}