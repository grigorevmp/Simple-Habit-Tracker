package com.grigorevmp.habits.presentation.screen.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun SettingsModule() {
    Surface(Modifier.fillMaxSize()) {
        Column {
            Text(
                modifier = Modifier.padding(16.dp), text = "Settings", fontSize = 24.sp
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun SettingsModulePreview() {
    SettingsModule()
}
