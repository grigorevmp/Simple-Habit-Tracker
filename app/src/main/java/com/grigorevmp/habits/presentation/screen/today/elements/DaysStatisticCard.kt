package com.grigorevmp.habits.presentation.screen.today.elements

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grigorevmp.habits.presentation.screen.today.data.HabitStatisticItemUi
import java.util.Calendar

@Composable
fun DaysStatisticCard(
    allHabitsStatisticData: List<HabitStatisticItemUi>,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Column {
            val calendar = Calendar.getInstance()
            calendar.firstDayOfWeek = Calendar.MONDAY
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

            val currentDate = Calendar.getInstance()

            val allHabits = allHabitsStatisticData.sumOf { it.habitsNumber }
            val allHabitsChecked = allHabitsStatisticData.sumOf { it.checkedHabitsNumber }

            val text = if (allHabits > 0) (allHabitsChecked.toFloat() / allHabits * 100).toInt() else 0

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 16.dp),
            ){
                Text(
                    fontSize = 20.sp,
                    text = "Week №${currentDate.get(Calendar.WEEK_OF_YEAR)}"
                )

                Spacer(
                    Modifier.weight(1F)
                )

                Text(
                    textAlign = TextAlign.End,
                    fontSize = 20.sp,
                    text = "Totally: $text%"
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                val percents = allHabitsStatisticData.map {
                    if (it.habitsNumber > 0) it.checkedHabitsNumber.toFloat() / it.habitsNumber.toFloat() * 100 else -1
                }
                for (percent in percents) {
                    Circle(percent.toInt())
                }
            }
        }
    }
}


@Composable
fun Circle(percent: Int, modifier: Modifier = Modifier) {
    val color by animateColorAsState(
        when (percent) {
            -1 -> Color(red = 0, green = 0, blue = 0, alpha = 0)
            0 -> Color(red = 0, green = 0, blue = 0, alpha = 25)
            100 -> MaterialTheme.colorScheme.secondary
            in 1..99 -> MaterialTheme.colorScheme.secondary
            else -> Color(red = 0, green = 0, blue = 0, alpha = 25)
        }, label = "circleColor"
    )

    val size by animateDpAsState(
        when (percent) {
            -1 -> 0.dp
            0 -> 24.dp
            100 -> 96.dp
            in 1..99 -> ((percent / 100f) * 96).dp
            else -> 24.dp
        }, label = "circleSize"
    )

    Canvas(
        modifier = modifier
            .size(48.dp)
            .then(modifier)
    ) {
        drawCircle(
            color = Color(red = 0, green = 0, blue = 0, alpha = 25),
            center = center,
            radius = 96.dp.value / 2,
            style = Fill
        )
        drawCircle(
            color = color,
            center = center,
            radius = size.value / 2,
            style = Fill
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DaysStatisticCardPreview() {
    DaysStatisticCard(
        listOf(
            HabitStatisticItemUi(4, 5, 1),
            HabitStatisticItemUi(0, 5, 2),
            HabitStatisticItemUi(0, 0, 3),
            HabitStatisticItemUi(3, 5, 4),
            HabitStatisticItemUi(5, 5, 5),
            HabitStatisticItemUi(4, 8, 6),
            HabitStatisticItemUi(5, 0, 7),
        )
    )
}