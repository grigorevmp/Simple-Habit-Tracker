package com.grigorevmp.habits.presentation.screen.habits.data


data class StatDay(
    val index: Int,
    val percent: Float,
)

data class StatMonth(
    val index: Int,
    val percent: Float,
    val days: List<StatDay>,
)

data class StatYear(
    val year: Int,
    val months: List<StatMonth>
)