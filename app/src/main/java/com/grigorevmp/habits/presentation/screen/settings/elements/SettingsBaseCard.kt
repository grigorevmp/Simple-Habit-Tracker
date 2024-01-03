package com.grigorevmp.habits.presentation.screen.settings.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grigorevmp.habits.R


@Composable
fun SettingsBaseCard(
    cardTitle: String,
    cardIconResource: Int,
    cardIconDescription: String,
    modifier: Modifier = Modifier,
    cardColor: CardColors = CardDefaults.cardColors(),
    cardOnClick: () -> Unit = { },
    content: @Composable () -> Unit = { },
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = cardColor,
        onClick = cardOnClick
    ) {
        Row {
            Icon(
                painter = painterResource(id = cardIconResource),
                contentDescription = cardIconDescription,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 16.dp),
            )

            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                Text(
                    text = cardTitle,
                    fontSize = 20.sp
                )

                content()
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SettingsBaseCardPreview() {
    SettingsBaseCard(
        cardTitle = "Settings title",
        cardIconResource = R.drawable.ic_settings,
        cardIconDescription = "Test description",
        cardColor = CardDefaults.cardColors(),
        cardOnClick = { },
    ) {
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = stringResource(R.string.settings_screen_up_to_date_summary),
        )
    }
}