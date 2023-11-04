package com.grigorevmp.habits.core.in_app_bus

data class Event (
    val eventType: EventType,
    val content: String,
)