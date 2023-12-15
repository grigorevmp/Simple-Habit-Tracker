package com.grigorevmp.habits.presentation.screen.settings.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grigorevmp.habits.R
import com.grigorevmp.habits.presentation.screen.settings.elements.SettingsBaseCard
import com.grigorevmp.habits.presentation.theme.ThemePreference

@Composable
fun ThemeChooserCard(
    getTheme: () -> ThemePreference,
    setTheme: (ThemePreference) -> Unit
) {
    val availableThemes = mapOf(
        ThemePreference.System to stringResource(R.string.settings_screen_theme_system),
        ThemePreference.Dark to stringResource(R.string.settings_screen_theme_dark),
        ThemePreference.Light to stringResource(R.string.settings_screen_theme_light)
    )

    val currentTheme = remember { mutableStateOf(getTheme()) }

    val isDropdownExpanded = remember {
        mutableStateOf(false)
    }

    SettingsBaseCard(
        cardTitle = stringResource(R.string.settings_screen_theme_title),
        cardIconResource = R.drawable.ic_palette,
        cardIconDescription = stringResource(R.string.settings_screen_theme_icon_description),
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
                    text = availableThemes[currentTheme.value] ?: stringResource(R.string.settings_screen_theme_system)
                )
            }
            MaterialTheme(
                shapes = MaterialTheme.shapes.copy(extraSmall = RoundedCornerShape(16.dp)),
            ) {
                DropdownMenu(
                    expanded = isDropdownExpanded.value,
                    onDismissRequest = { isDropdownExpanded.value = !isDropdownExpanded.value },
                ) {
                    availableThemes.forEach { (key) ->
                        availableThemes[key]?.let { themeName ->
                            DropdownMenuItem(
                                text = { Text(themeName) },
                                onClick = {
                                    setTheme(key)
                                    currentTheme.value = key
                                    isDropdownExpanded.value = !isDropdownExpanded.value
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ThemeChooserCardPreview() {
    ThemeChooserCard(
        setTheme = { _ -> },
        getTheme = { ThemePreference.System },
    )
}