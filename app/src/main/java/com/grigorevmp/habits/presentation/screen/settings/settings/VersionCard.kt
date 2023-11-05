package com.grigorevmp.habits.presentation.screen.settings.settings

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.grigorevmp.habits.R
import com.grigorevmp.habits.presentation.screen.settings.elements.SettingsBaseCard

@Composable
fun VersionCard() {
    SettingsBaseCard(
        cardTitle = stringResource(R.string.settings_screen_version_title),
        cardIconResource = R.drawable.ic_info,
        cardIconDescription = stringResource(R.string.settings_screen_version_settings_icon_description),
        cardColor = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
    ) {


        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(R.string.settings_screen_version)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun VersionCardPreview() {
    VersionCard()
}