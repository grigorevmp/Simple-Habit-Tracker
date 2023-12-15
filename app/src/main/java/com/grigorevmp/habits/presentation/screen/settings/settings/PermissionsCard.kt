package com.grigorevmp.habits.presentation.screen.settings.settings

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.grigorevmp.habits.R
import com.grigorevmp.habits.core.utils.Utils
import com.grigorevmp.habits.presentation.screen.settings.elements.SettingsBaseCard


@SuppressLint("BatteryLife")
@Composable
fun PermissionsCard(
    isIgnoringBattery: (Context) -> Boolean,
    getPackageName: (Context) -> String,
) {
    val context = LocalContext.current

    val isTiramisuPlus =
        remember { mutableStateOf(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) }
    val isCantPostNotifications = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            )
        } else {
            mutableStateOf(true)
        }
    }

    var canScheduleExactAlarms by remember {
        mutableStateOf(false)
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        (context.getSystemService() as AlarmManager?)?.also {
            canScheduleExactAlarms = it.canScheduleExactAlarms()
        }
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        isCantPostNotifications.value = !it
    }

    val isNotIgnoreBattery = remember { mutableStateOf(!isIgnoringBattery(context)) }

    val launcherBattery =
        rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                isNotIgnoreBattery.value = false
            } else {
                isNotIgnoreBattery.value = !isIgnoringBattery(context)
            }
        }

    val launcherExactAlarm =
        rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                isNotIgnoreBattery.value = false
            } else {
                isNotIgnoreBattery.value = !isIgnoringBattery(context)
            }
        }

    Log.d("Settings", "==== Permission block ====")
    Log.d("Settings", "Tiramisu+: ${isTiramisuPlus.value}")
    Log.d("Settings", "Post notifications: ${!isCantPostNotifications.value}")
    Log.d("Settings", "Battery optimization: ${!isNotIgnoreBattery.value}")
    Log.d("Settings", "Can schedule exact alarms: $canScheduleExactAlarms")
    Log.d("Settings", "==== ================ ====")

    if ((isTiramisuPlus.value && isCantPostNotifications.value) || isNotIgnoreBattery.value) {
        LaunchedEffect(Unit) {
            Log.d("Permission card", "Warning card: true")
            Utils.isWarningSettings.emit(true)
        }

        SettingsBaseCard(
            cardTitle = stringResource(R.string.settings_screen_permissions_title),
            cardIconResource = R.drawable.ic_warning,
            cardIconDescription = stringResource(R.string.settings_screen_up_to_date_icon_description),
            cardColor = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            ),
            cardOnClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (isTiramisuPlus.value && isCantPostNotifications.value) {
                        launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }

                if (isNotIgnoreBattery.value) {
                    val intent = Intent()
                    intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.data = Uri.parse("package:${getPackageName(context)}")
                    val pendIntent =
                        PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
                    ContextCompat.startActivity(context, intent, null)

                    launcherBattery.launch(
                        IntentSenderRequest.Builder(pendIntent).build()
                    )
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (!canScheduleExactAlarms) {
                        val intent = Intent()
                        intent.action = ACTION_REQUEST_SCHEDULE_EXACT_ALARM

                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        val pendIntent =
                            PendingIntent.getActivity(
                                context,
                                0,
                                intent,
                                PendingIntent.FLAG_IMMUTABLE
                            )
                        ContextCompat.startActivity(context, intent, null)

                        launcherExactAlarm.launch(
                            IntentSenderRequest.Builder(pendIntent).build()
                        )
                    }
                }
            },
        ) {
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = stringResource(R.string.settings_screen_permission_summary),
            )
        }
    } else {
        LaunchedEffect(Unit) {
            Log.d("Permission card", "Warning card: true")
            Utils.isWarningSettings.emit(false)
        }

        SettingsBaseCard(
            cardTitle = stringResource(R.string.settings_screen_up_to_date_title),
            cardIconResource = R.drawable.ic_done,
            cardIconDescription = stringResource(R.string.settings_screen_up_to_date_icon_description),
        ) {
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = stringResource(R.string.settings_screen_up_to_date_summary),
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PermissionsCardPreview() {
    PermissionsCard(
        { _ -> false }, { _ -> "" }
    )
}