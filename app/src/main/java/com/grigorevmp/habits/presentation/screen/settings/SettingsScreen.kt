package com.grigorevmp.habits.presentation.screen.settings

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.grigorevmp.habits.R


@Composable
fun SettingsScreen(vm: SettingsScreenViewModel) {
    SettingsScreen(
        vm::isIgnoringBattery,
        vm::getPackageName,
    )
}

@Composable
fun SettingsScreen(
    isIgnoringBattery: (Context) -> Boolean,
    getPackageName: (Context) -> String,
) {
    Surface(Modifier.fillMaxSize()) {
        Column {
            Text(
                modifier = Modifier.padding(16.dp),
                text = stringResource(R.string.settings_screen_settings_title),
                fontSize = 24.sp
            )

            PermissionsCard(
                isIgnoringBattery,
                getPackageName,
            )

            Spacer(
                Modifier.weight(1f)
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Center,
                text = stringResource(R.string.settings_screen_version)
            )
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionsCard(
    isIgnoringBattery: (Context) -> Boolean,
    getPackageName: (Context) -> String,
) {
    val context = LocalContext.current

    val isTiramisuPlus = remember { mutableStateOf(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) }
    val isCantPostNotifications = remember { mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        isCantPostNotifications.value = !it
    }

    val isNotIgnoreBattery = remember { mutableStateOf(!isIgnoringBattery(context)) }

    val launcherBattery = rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
        if (it.resultCode == RESULT_OK) {
            isNotIgnoreBattery.value = false
        } else {
            isNotIgnoreBattery.value = !isIgnoringBattery(context)
        }
    }

    Log.d("Settings", "==== Permission block ====")
    Log.d("Settings", "Tiramisu+: ${isTiramisuPlus.value}")
    Log.d("Settings", "Post notifications: ${!isCantPostNotifications.value}")
    Log.d("Settings", "Battery optimization: ${!isNotIgnoreBattery.value}")
    Log.d("Settings", "==== ================ ====")

    if ((isTiramisuPlus.value && isCantPostNotifications.value) || isNotIgnoreBattery.value) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            ),
            onClick = {
                if (isTiramisuPlus.value && isCantPostNotifications.value) {
                    launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }

                if (isNotIgnoreBattery.value) {
                    val intent = Intent()
                    intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.data = Uri.parse("package:${getPackageName(context)}")
                    val pendIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
                    startActivity(context, intent, null)

                    launcherBattery.launch(
                        IntentSenderRequest.Builder(pendIntent).build()
                    )

                }
            }
        ) {
            Row {
                Icon(
                    Icons.Filled.Warning, contentDescription = stringResource(R.string.settings_screen_permissions_required_icon_description),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 16.dp),
                )

                Column(
                    modifier = Modifier.padding(16.dp),
                ) {
                    Text(
                        text = stringResource(R.string.settings_screen_permissions_title),
                        fontSize = 20.sp
                    )
                    Text(
                        modifier = Modifier.padding(top = 8.dp),
                        text = stringResource(R.string.settings_screen_permission_summary),
                    )
                }
            }

        }
    } else {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Row {
                Icon(
                    Icons.Filled.Done, contentDescription = stringResource(R.string.settings_screen_up_to_date_icon_description),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 16.dp),
                )

                Column(
                    modifier = Modifier.padding(16.dp),
                ) {
                    Text(
                        text = stringResource(R.string.settings_screen_up_to_date_title),
                        fontSize = 20.sp
                    )
                    Text(
                        modifier = Modifier.padding(top = 8.dp),
                        text = stringResource(R.string.settings_screen_up_to_date_summary),
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(
        { _ -> false }, { _ -> ""}
    )
}

