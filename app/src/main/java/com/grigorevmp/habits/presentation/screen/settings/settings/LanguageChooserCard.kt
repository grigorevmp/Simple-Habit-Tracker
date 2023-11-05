package com.grigorevmp.habits.presentation.screen.settings.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.ConfigurationCompat
import com.grigorevmp.habits.R
import com.grigorevmp.habits.core.utils.Utils
import com.grigorevmp.habits.presentation.screen.settings.elements.SettingsBaseCard
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageChooserCard() {
    val availableLanguages = listOf("ru" to "Русский", "en" to "English")
    val context = LocalContext.current

    val configuration = LocalConfiguration.current
    val locale = ConfigurationCompat.getLocales(configuration).get(0)?.language ?: "en"

    val selectedLanguage = remember {
        mutableStateOf(locale)
    }

    val selectedLanguageReadable = remember {
        mutableStateOf(
            ConfigurationCompat.getLocales(configuration)
                .get(0)?.displayName?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
                ?: "English"
        )
    }

    val isDropdownExpanded = remember {
        mutableStateOf(false)
    }

    SettingsBaseCard(
        cardTitle = stringResource(R.string.settings_screen_language),
        cardIconResource = R.drawable.ic_language,
        cardIconDescription = stringResource(R.string.settings_screen_language_icon_description),
        cardColor = CardDefaults.cardColors(),
        cardOnClick = { },
    ) {
        Box {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
                onClick = {
                    isDropdownExpanded.value = !isDropdownExpanded.value
                }
            ) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = selectedLanguageReadable.value
                )
            }
            MaterialTheme(
                shapes = MaterialTheme.shapes.copy(extraSmall = RoundedCornerShape(16.dp)),
            ) {
                DropdownMenu(
                    expanded = isDropdownExpanded.value,
                    onDismissRequest = { isDropdownExpanded.value = !isDropdownExpanded.value },
                ) {
                    availableLanguages.forEach { language ->
                        DropdownMenuItem(
                            text = { Text(language.second) },
                            onClick = {
                                selectedLanguage.value = language.first

                                Utils.localeSelection(context, selectedLanguage.value)
                            }
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LanguageChooserCardPreview() {
    LanguageChooserCard()
}