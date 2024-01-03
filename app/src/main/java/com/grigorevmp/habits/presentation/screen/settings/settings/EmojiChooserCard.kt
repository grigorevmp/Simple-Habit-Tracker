package com.grigorevmp.habits.presentation.screen.settings.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.grigorevmp.habits.R
import com.grigorevmp.habits.core.utils.Utils.fullEmojiRegex
import com.grigorevmp.habits.presentation.screen.settings.elements.SettingsBaseCard

@Composable
fun EmojiChooserCard(
    getCongratsEmoji: () -> Set<String>,
    setCongratsEmoji: (Set<String>) -> Unit,
) {

    SettingsBaseCard(
        cardTitle = stringResource(R.string.settings_screen_emoji_title),
        cardIconResource = R.drawable.ic_face,
        cardIconDescription = stringResource(R.string.settings_screen_emoji_energy_icon_description),
        cardColor = CardDefaults.cardColors(),
        cardOnClick = { },
    ) {
        var currentEmojiSet by remember { mutableStateOf(getCongratsEmoji()) }
        var showEmojiDialog by remember { mutableStateOf(false) }
        var isError by rememberSaveable { mutableStateOf(false) }
        var title by remember { mutableStateOf(currentEmojiSet.joinToString(", ")) }

        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = stringResource(R.string.settings_screen_emoji_summary),
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
            onClick = {
                showEmojiDialog = true
            }
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = currentEmojiSet.joinToString(", ")
            )
        }

        fun validate(input: String): Boolean {
            val emojis = input.split(", ").map { it.trim() }
            val emojiRegex = Regex(fullEmojiRegex)

            isError = !(emojis.all { emojiRegex.matches(it) })
            return !isError
        }

        if (showEmojiDialog) {
            Dialog(onDismissRequest = { showEmojiDialog = false }) {
                Surface(
                    modifier = Modifier.clip(RoundedCornerShape(12.dp)),
                    color = MaterialTheme.colorScheme.surfaceContainerHighest,
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    ) {
                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 20.dp),
                            text = stringResource(R.string.settings_screen_emoji_dialog_title),
                            fontSize = 24.sp
                        )
                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(8.dp),
                            text = stringResource(R.string.settings_screen_emoji_dialog_summary),
                        )

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp),
                                shape = RoundedCornerShape(8.dp),
                                value = title,
                                isError = isError,
                                supportingText = {
                                    if (isError) {
                                        Text(
                                            modifier = Modifier.fillMaxWidth(),
                                            text = stringResource(R.string.settings_screen_emoji_dialog_hint),
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
                                },
                                trailingIcon = {
                                    if (isError) Icon(
                                        Icons.Filled.Info,
                                        stringResource(R.string.habit_screen_edit_habit_error_icon_description),
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                },
                                onValueChange = { newValue ->
                                    title = newValue
                                },
                                label = { Text(stringResource(R.string.settings_screen_emoji_dialog_label)) },
                                colors = TextFieldDefaults.colors(
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent,
                                    errorIndicatorColor = Color.Transparent,
                                ),
                            )
                        }

                        Button(
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(bottom = 8.dp),
                            onClick = {
                                if (validate(title)) {
                                    currentEmojiSet = title.split(", ").toSet()
                                    setCongratsEmoji(currentEmojiSet)
                                    showEmojiDialog = false
                                }
                            }
                        ) {
                            Text(stringResource(R.string.settings_screen_emoji_dialog_save_button))
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun EmojiChooserCardPreview() {
    EmojiChooserCard(
        getCongratsEmoji = { setOf("🎊", "🔥") },
        setCongratsEmoji = { _ -> },
    )
}