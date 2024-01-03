package com.grigorevmp.habits.presentation.screen.today.elements

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grigorevmp.habits.R
import com.grigorevmp.habits.presentation.screen.today.data.HabitWithDatesUI

@Composable
fun NoHabitsForDayCard() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary
        ),
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Icon(
                painter = painterResource(id = R.drawable.ic_today),
                contentDescription = stringResource(R.string.reminder_icon_description),
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )

            Text(
                text = stringResource(R.string.today_screen_habit_card_no_planned_habits),
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(horizontal = 8.dp),
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HabitsForDateCardEmptyPreview() {
    HabitsForDayCard(
        habitsForDate = HabitWithDatesUI(
            habits = mutableListOf(),
            date = "2023-10-28",
        ),
        updateHabitRef = { _, _, _ -> },
        updateHabitRefCountable = { _, _, _ , _ -> },
    )
}