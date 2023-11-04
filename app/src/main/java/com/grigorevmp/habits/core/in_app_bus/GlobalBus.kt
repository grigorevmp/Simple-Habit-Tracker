package com.grigorevmp.habits.core.in_app_bus

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

object GlobalBus {

    private val data = MutableSharedFlow<Event>()

    fun post(event: Event) {
        CoroutineScope(Dispatchers.IO).launch {
            data.emit(event)
        }
    }

    fun postWithDelay(event: Event, delayValue: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            delay(delayValue)
            data.emit(event)
        }
    }

    fun events() = flow {
        data.collect {
            emit(it)
        }
    }
}