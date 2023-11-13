package com.grigorevmp.habits.presentation.screen.habits.elements.stastistic

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.ConfigurationCompat
import com.grigorevmp.habits.R
import com.grigorevmp.habits.presentation.screen.habits.data.StatYear
import com.grigorevmp.habits.presentation.screen.habits.elements.common.ActionIcon
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.line.lineSpec
import com.patrykandpatrick.vico.compose.component.shape.shader.verticalGradient
import com.patrykandpatrick.vico.core.chart.layout.HorizontalLayout
import com.patrykandpatrick.vico.core.chart.values.AxisValuesOverrider
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entryOf
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun MonthCard(
    stats: List<StatYear>?
) {
    val charModelList = stats ?: return

    val chartColor = MaterialTheme.colorScheme.primary

    val monthMain by remember { mutableStateOf(LocalDate.now()) }

    val chartModelDefault = (0 until 32).map { entryOf(it, 0) }.toMutableList()

    val monthsValues =
        charModelList.firstOrNull { it.year == LocalDate.now().year }?.months ?: return


    val months = monthsValues.map {
        it.index
    }

    val daysCount = monthMain.lengthOfMonth()

    monthsValues.firstOrNull { it.index == monthMain.monthValue - 1 }?.days?.forEach {
        chartModelDefault[it.index] = entryOf(
            it.index, it.percent
        )
    }

    println(monthsValues)
    println(chartModelDefault)

    Column {

        Chart(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 36.dp, end = 8.dp),
            horizontalLayout = HorizontalLayout.FullWidth(),
            isZoomEnabled = true,
            bottomAxis = rememberBottomAxis(valueFormatter = { value, _ ->
                if (value.toInt() in 0..daysCount) {
                    value.toInt().toString()
                } else {
                    ""
                }
            }),
            chart = lineChart(
                lines = listOf(
                    lineSpec(
                        lineColor = chartColor,
                        lineBackgroundShader = verticalGradient(
                            arrayOf(chartColor.copy(0.5f), chartColor.copy(alpha = 0f)),
                        ),
                    ),
                    lineSpec(
                        lineColor = chartColor,
                        lineBackgroundShader = verticalGradient(
                            arrayOf(chartColor.copy(0.5f), chartColor.copy(alpha = 0f)),
                        ),
                    ),
                ), axisValuesOverrider = AxisValuesOverrider.fixed(
                    minY = 0f,
                    maxY = 1f,
                    minX = 0f,
                    maxX = daysCount.toFloat(),
                )
            ),
            model = ChartEntryModelProducer(listOf(chartModelDefault)).requireModel(),
        )

        Row(
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))

            ActionIcon(
                iconId = R.drawable.ic_left,
                iconDescription = stringResource(R.string.icon_left_description)
            ) {
                if (monthMain.monthValue - 1 in months) {
                    monthMain.minusMonths(1)
                }
            }

            val configuration = LocalConfiguration.current
            val locale = ConfigurationCompat.getLocales(configuration).get(0)?.language ?: "en"

            Text(
                text = monthMain.month.getDisplayName(
                    TextStyle.FULL_STANDALONE,
                    Locale.forLanguageTag(locale)
                ).replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
                   },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .align(Alignment.CenterVertically),
            )

            ActionIcon(
                iconId = R.drawable.ic_right,
                iconDescription = stringResource(R.string.icon_right_description)
            ) {
                if (monthMain.monthValue + 1 in months) {
                    monthMain.plusMonths(1)
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
@Preview
fun MonthCardPreview() {
    MonthCard(
        stats = listOf(
            StatYear(
                year = 2023,
                months = listOf()
            )
        )
    )
}