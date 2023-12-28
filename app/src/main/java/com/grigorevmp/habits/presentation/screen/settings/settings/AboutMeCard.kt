package com.grigorevmp.habits.presentation.screen.settings.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.grigorevmp.habits.R
import com.grigorevmp.habits.presentation.screen.settings.elements.SettingsBaseCard

@Composable
fun AboutMeCard() {
    val context = LocalContext.current
    val telegramUrl = "https://t.me/grigorevmp"

    SettingsBaseCard(
        cardTitle = stringResource(R.string.screen_settings_about_title),
        cardIconResource = R.drawable.ic_telegram,
        cardIconDescription = stringResource(R.string.settings_screen_version_settings_icon_description),
        cardColor = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        cardOnClick = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(telegramUrl))
            context.startActivity(intent)
        }
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(R.string.screen_settings_about_description)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun AboutMeCardPreview() {
    AboutMeCard()
}