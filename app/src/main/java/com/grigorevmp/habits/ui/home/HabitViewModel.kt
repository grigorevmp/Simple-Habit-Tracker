package com.grigorevmp.habits.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grigorevmp.habits.data.HabitEntity
import com.grigorevmp.habits.data.repository.DateRepository
import com.grigorevmp.habits.data.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HabitViewModel @Inject constructor(
    private var repository: HabitRepository,
    private var dateRepository: DateRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val allHabits = repository.allHabits

    fun addHabit(title: String, description: String) {
        viewModelScope.launch {
            val habit = HabitEntity(
                title = title,
                description = description,
                completed = false
            )
            repository.insert(habit)
        }
    }

    fun getDate(date: LocalDate) = flow {
        emit(dateRepository.getDateId(date))
    }.flowOn(Dispatchers.IO)

    fun getHabitWithDate(dateId: Long, habitId: Long) = flow {
        emit(repository.getHabitForDate(dateId, habitId))
    }.flowOn(Dispatchers.IO)

    fun updateHabit(habit: HabitEntity) {
        viewModelScope.launch {
            repository.update(habit)
        }
    }

//    fun scheduleNotification(context: Context, id: Int, title: String, timeInMillis: Long) {
//        val intent = Intent(context, AlarmReceiver::class.java).apply {
//            action = "com.example.NOTIFY"
//            putExtra("NOTIFICATION_ID", id)
//            putExtra("NOTIFICATION_TITLE", title)
//        }
//
//        val pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//
//        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
//    }

    fun insert(habit: HabitEntity) = viewModelScope.launch {
        repository.insert(habit)
    }

    fun update(habit: HabitEntity) = viewModelScope.launch {
        repository.update(habit)
    }

    fun delete(habit: HabitEntity) = viewModelScope.launch {
        repository.delete(habit)
    }
}


//class AlarmReceiver : BroadcastReceiver() {
//    override fun onReceive(context: Context, intent: Intent) {
//        val id = intent.getIntExtra("NOTIFICATION_ID", 0)
//        val title = intent.getStringExtra("NOTIFICATION_TITLE")
//
//        val notification = NotificationCompat.Builder(context, "notification_channel")
//            .setContentTitle(title)
//            .setContentText("It's time to perform this habit!")
//            .setSmallIcon(R.drawable.ic_launcher_background)
//            .build()
//
//        val notificationManager = NotificationManagerCompat.from(context)
//        notificationManager.notify(id, notification)
//    }
//}