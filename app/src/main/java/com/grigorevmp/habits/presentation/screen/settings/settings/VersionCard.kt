package com.grigorevmp.habits.presentation.screen.settings.settings

import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grigorevmp.habits.R
import com.grigorevmp.habits.core.utils.Changelogs
import com.grigorevmp.habits.presentation.screen.settings.elements.SettingsBaseCard

@Composable
fun VersionCard() {
    var currentVersion by remember {
        mutableStateOf(false)
    }

    SettingsBaseCard(
        cardTitle = stringResource(R.string.settings_screen_version_title),
        cardIconResource = R.drawable.ic_info,
        cardIconDescription = stringResource(R.string.settings_screen_version_settings_icon_description),
        cardColor = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        cardOnClick = {
            currentVersion = true
        }
    ) {
        val context = LocalContext.current
        val manager = context.packageManager
        val info = manager.getPackageInfo(context.packageName, PackageManager.GET_ACTIVITIES) ?: return@SettingsBaseCard

        if(currentVersion) {
            BottomVersionDialog(
                { currentVersion = false }
            ) {
                Changelogs.version.toLong()
            }
        }

        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(R.string.settings_screen_version, info.versionName.split("-")[0])
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun BottomVersionDialog(hideDialog: () -> Unit, getVersion: () -> Long) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current

    val changelogs = getChangelogForVersions(context, version = getVersion())

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            hideDialog()
        },
    ) {
        Icon(
            Icons.Filled.Refresh,
            contentDescription = "Update icon",
            Modifier
                .size(64.dp)
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 8.dp)
        )

        Text(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .align(Alignment.CenterHorizontally),
            text = stringResource(R.string.new_features),
            fontSize = 24.sp
        )
        ChangelogSheet(changelogs = changelogs)
        Button(
            modifier = Modifier
                .padding(top = 8.dp)
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            onClick = {
                hideDialog()
            }) {
            Text(stringResource(R.string.cancel_button))
        }
        Spacer(modifier = Modifier.height(32.dp))
    }

}

@Composable
fun ChangelogSheet(changelogs: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
            .padding(horizontal = 24.dp)
    ) {
        for (changelog in changelogs) {
            Text(text = changelog, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

fun getChangelogForVersions(context: Context, version: Long): List<String> {
    val startIndex = version.toInt() - 1
    if (startIndex < 0 || startIndex >= Changelogs.getVersions(context).size) {
        return emptyList()
    }
    return Changelogs.getVersions(context).subList(startIndex, Changelogs.getVersions(context).size)
}


@Preview(showBackground = true)
@Composable
fun VersionCardPreview() {
    VersionCard()
}