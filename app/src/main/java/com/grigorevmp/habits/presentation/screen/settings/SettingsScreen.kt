package com.grigorevmp.habits.presentation.screen.settings

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grigorevmp.habits.R
import com.grigorevmp.habits.presentation.screen.settings.settings.EmojiChooserCard
import com.grigorevmp.habits.presentation.screen.settings.settings.LanguageChooserCard
import com.grigorevmp.habits.presentation.screen.settings.settings.PermissionsCard
import com.grigorevmp.habits.presentation.screen.settings.settings.VersionCard


@Composable
fun SettingsScreen(vm: SettingsScreenViewModel) {
    SettingsScreen(
        vm::isIgnoringBattery,
        vm::getPackageName,
        vm::getCongratsEmoji,
        vm::setCongratsEmoji,
    )
}

@Composable
fun SettingsScreen(
    isIgnoringBattery: (Context) -> Boolean,
    getPackageName: (Context) -> String,
    getCongratsEmoji: () -> Set<String>,
    setCongratsEmoji: (Set<String>) -> Unit,
) {
    Surface(Modifier.fillMaxSize()) {
        Column {
            Text(
                modifier = Modifier.padding(16.dp),
                text = stringResource(R.string.settings_screen_settings_title),
                fontSize = 24.sp
            )

            VersionCard()

            PermissionsCard(
                isIgnoringBattery,
                getPackageName,
            )

            LanguageChooserCard()

            EmojiChooserCard(
                getCongratsEmoji,
                setCongratsEmoji
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(
        { _ -> false }, { _ -> "" }, { setOf("") }, { _ -> }
    )
}

