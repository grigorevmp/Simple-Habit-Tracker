package com.grigorevmp.habits.presentation.screen.settings.settings

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.grigorevmp.habits.R
import com.grigorevmp.habits.presentation.screen.settings.elements.SettingsBaseCardWithToggle

@Composable
fun LongerDateCard(
    switchState: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    SettingsBaseCardWithToggle(
        cardTitle = stringResource(R.string.settings_screen_extend_the_day_title),
        cardIconResource = R.drawable.ic_today,
        cardIconDescription = stringResource(R.string.settings_screen_calendar_icon_description),
        switchState = switchState,
        onCheckedChange = onCheckedChange,
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(R.string.settings_screen_extend_the_day_summary)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun LongerDateCardPreview() {
    LongerDateCard(
        switchState = true,
        onCheckedChange = { },
    )
}