package com.grigorevmp.habits.presentation.screen.settings.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
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
fun SettingsBaseCardWithToggle(
    cardTitle: String,
    cardIconResource: Int,
    cardIconDescription: String,
    switchState: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    cardColor: CardColors = CardDefaults.cardColors(),
    content: @Composable () -> Unit = { },
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = {
            onCheckedChange(!switchState)
        },
        colors = cardColor,
    ) {
        Row {
            Icon(
                painter = painterResource(id = cardIconResource),
                contentDescription = cardIconDescription,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 16.dp),
            )

            Spacer(Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .weight(1f)
                    .padding(end = 10.dp),
            ) {
                Text(
                    text = cardTitle,
                    fontSize = 20.sp
                )

                content()
            }

            Switch(
                checked = switchState,
                onCheckedChange = onCheckedChange,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SettingsBaseCardWithTogglePreview() {
    SettingsBaseCardWithToggle(
        cardTitle = "Settings title",
        cardIconResource = R.drawable.ic_settings,
        cardIconDescription = "Test description",
        switchState = true,
        onCheckedChange = { },
        cardColor = CardDefaults.cardColors(),
    ) {
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = stringResource(R.string.settings_screen_up_to_date_summary),
        )
    }
}