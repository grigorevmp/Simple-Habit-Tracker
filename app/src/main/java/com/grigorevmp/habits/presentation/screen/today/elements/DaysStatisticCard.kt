package com.grigorevmp.habits.presentation.screen.today.elements

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.grigorevmp.habits.R
import com.grigorevmp.habits.presentation.screen.common.EmojiView
import com.grigorevmp.habits.presentation.screen.habits.parseToDate
import com.grigorevmp.habits.presentation.screen.today.data.HabitStatisticItemUi
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.line.lineSpec
import com.patrykandpatrick.vico.compose.component.shape.shader.verticalGradient
import com.patrykandpatrick.vico.core.chart.layout.HorizontalLayout
import com.patrykandpatrick.vico.core.chart.values.AxisValuesOverrider
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.entryOf
import java.time.DayOfWeek
import java.util.Calendar

@Composable
fun DaysStatisticCard(
    allHabitsStatisticData: List<HabitStatisticItemUi>,
    modifier: Modifier = Modifier,
    getRandomEmoji: (Long) -> String,
) {
    val isStatisticsOpened = remember {
        mutableStateOf(false)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .padding(bottom = 8.dp),
        onClick = {
            isStatisticsOpened.value = !isStatisticsOpened.value
        }
    ) {
        Column {
            val calendar = Calendar.getInstance()
            calendar.firstDayOfWeek = Calendar.MONDAY
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

            val currentDate = Calendar.getInstance()

            val yellow = MaterialTheme.colorScheme.primary
            val pink = MaterialTheme.colorScheme.secondary
            val context = LocalContext.current

            val allHabits = allHabitsStatisticData.sumOf { it.habitsNumber }
            val allHabitsChecked = allHabitsStatisticData.sumOf { it.checkedHabitsNumber }

            val result = allHabitsStatisticData.mapIndexed { index, habitStatisticItemUi ->
                entryOf(
                    index,
                    if (habitStatisticItemUi.habitsNumber == 0) {
                        0
                    } else {
                        habitStatisticItemUi.checkedHabitsNumber.toFloat() / habitStatisticItemUi.habitsNumber * 100
                    }
                )
            }

            val text =
                if (allHabits > 0) (allHabitsChecked.toFloat() / allHabits * 100).toInt() else 0

            DaysTitleCard(currentDate, text)

            CirclesWithDaysProgress(allHabitsStatisticData, getRandomEmoji)

            WeekProgressChart(
                isStatisticsOpened,
                context,
                yellow,
                pink,
                result
            )

            AnimatedVisibility(
                visible = !isStatisticsOpened.value
            ) {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun ColumnScope.WeekProgressChart(
    isStatisticsOpened: MutableState<Boolean>,
    context: Context,
    yellow: Color,
    pink: Color,
    result: List<FloatEntry>
) {
    AnimatedVisibility(
        visible = isStatisticsOpened.value
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 26.dp)
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            for (day in DayOfWeek.entries) {
                Text(
                    text = day.parseToDate(context),
                    fontSize = 12.sp,
                )
            }
        }

        Chart(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 40.dp, top = 36.dp, end = 40.dp),
            isZoomEnabled = false,
            horizontalLayout = HorizontalLayout.FullWidth(),
            bottomAxis = rememberBottomAxis(
                valueFormatter = { _, _ -> "" },
            ),
            chart = lineChart(
                lines = listOf(
                    lineSpec(
                        lineColor = yellow,
                        lineBackgroundShader = verticalGradient(
                            arrayOf(yellow.copy(0.5f), yellow.copy(alpha = 0f)),
                        ),
                    ),
                    lineSpec(
                        lineColor = pink,
                        lineBackgroundShader = verticalGradient(
                            arrayOf(pink.copy(0.5f), pink.copy(alpha = 0f)),
                        ),
                    ),
                ),
                axisValuesOverrider = AxisValuesOverrider.fixed(
                    minY = 0f,
                    maxY = 100f,
                    minX = 0f,
                    maxX = 6f,
                )
            ),
            model = ChartEntryModelProducer(listOf(result)).requireModel(),
        )
    }
}

@Composable
private fun CirclesWithDaysProgress(
    allHabitsStatisticData: List<HabitStatisticItemUi>,
    getRandomEmoji: (Long) -> String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        val percents = allHabitsStatisticData.map {
            if (it.habitsNumber > 0) {
                (it.checkedHabitsNumber.toFloat() / it.habitsNumber.toFloat() * 100).toInt() to it.id
            } else {
                -1 to it.id
            }
        }
        for (percent in percents) {
            Circle(percent, getRandomEmoji)
        }
    }
}

@Composable
private fun DaysTitleCard(currentDate: Calendar, text: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 16.dp),
    ) {
        Text(
            fontSize = 20.sp,
            text = stringResource(
                R.string.today_screen_statistics_week,
                currentDate.get(Calendar.WEEK_OF_YEAR)
            )
        )

        Spacer(
            Modifier.weight(1F)
        )

        Text(
            textAlign = TextAlign.End,
            fontSize = 20.sp,
            text = stringResource(R.string.today_screen_statistics_totally_completed, text)
        )
    }
}


@Composable
fun Circle(
    percent: Pair<Int, Long>,
    getRandomEmoji: (Long) -> String,
    modifier: Modifier = Modifier,
) {
    val emoji by remember(percent.first != 100) { mutableStateOf(getRandomEmoji(percent.second)) }

    val color by animateColorAsState(
        when (percent.first) {
            -1 -> Color(red = 0, green = 0, blue = 0, alpha = 0)
            0 -> Color(red = 0, green = 0, blue = 0, alpha = 25)
            100 -> MaterialTheme.colorScheme.secondary
            in 1..99 -> MaterialTheme.colorScheme.secondary
            else -> Color(red = 0, green = 0, blue = 0, alpha = 25)
        }, label = "circleColor"
    )

    val size by animateDpAsState(
        when (percent.first) {
            -1 -> 0.dp
            0 -> 24.dp
            100 -> 96.dp
            in 1..99 -> ((percent.first / 100f) * 96).dp
            else -> 24.dp
        }, label = "circleSize"
    )

    Box(
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = modifier
                .size(48.dp)
                .then(modifier)
                .zIndex(-1F)
        ) {
            drawCircle(
                color = Color(red = 0, green = 0, blue = 0, alpha = 25),
                center = center,
                radius = 96.dp.value / 2,
                style = Fill
            )
        }

        AnimatedVisibility(
            visible = percent.first != 100,
            enter = fadeIn(
                animationSpec = TweenSpec(200, 0, FastOutSlowInEasing)
            ),
            exit = fadeOut(
                animationSpec = TweenSpec(200, 0, FastOutLinearInEasing)
            ),
        ) {
            Canvas(
                modifier = modifier
                    .size(48.dp)
                    .then(modifier)
                    .zIndex(-1F)
            ) {
                drawCircle(
                    color = color,
                    center = center,
                    radius = size.value / 2,
                    style = Fill
                )
            }
        }

        AnimatedVisibility(
            visible = percent.first == 100,
            enter = scaleIn(
                animationSpec = TweenSpec(400, 0, FastOutLinearInEasing)
            ),
            exit = scaleOut(
                animationSpec = TweenSpec(400, 0, FastOutSlowInEasing)
            ),
            modifier = Modifier.zIndex(4F)
        ) {
            EmojiView(emoji = emoji, fontSize = 16F)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DaysStatisticCardPreview() {
    DaysStatisticCard(
        allHabitsStatisticData = listOf(
            HabitStatisticItemUi(4, 5, 1, 1L),
            HabitStatisticItemUi(0, 5, 2, 1L),
            HabitStatisticItemUi(0, 0, 3, 1L),
            HabitStatisticItemUi(3, 5, 4, 1L),
            HabitStatisticItemUi(5, 5, 5, 1L),
            HabitStatisticItemUi(4, 8, 6, 1L),
            HabitStatisticItemUi(5, 0, 7, 1L),
        ),
        getRandomEmoji = { _ -> "" }
    )
}