package com.grigorevmp.habits.presentation.screen.settings.settings

import android.content.pm.PackageManager
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.grigorevmp.habits.R
import com.grigorevmp.habits.presentation.screen.settings.elements.SettingsBaseCardWithToggle

@Composable
fun LongerDateCard(
    switchState: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    SettingsBaseCardWithToggle(
        cardTitle = "Extend the day",
        cardIconResource = R.drawable.ic_today,
        cardIconDescription = "Today color description",
        switchState = switchState,
        onCheckedChange = onCheckedChange,
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = "Extend the current day and do not display new events until 3 am"
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