package com.grigorevmp.habits.presentation.screen.settings.settings

import android.Manifest
import android.app.Activity
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
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.grigorevmp.habits.R
import com.grigorevmp.habits.presentation.screen.settings.elements.SettingsBaseCard


@Composable
fun PermissionsCard(
    isIgnoringBattery: (Context) -> Boolean,
    getPackageName: (Context) -> String,
) {
    val context = LocalContext.current

    val isTiramisuPlus =
        remember { mutableStateOf(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) }
    val isCantPostNotifications = remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        )
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

    Log.d("Settings", "==== Permission block ====")
    Log.d("Settings", "Tiramisu+: ${isTiramisuPlus.value}")
    Log.d("Settings", "Post notifications: ${!isCantPostNotifications.value}")
    Log.d("Settings", "Battery optimization: ${!isNotIgnoreBattery.value}")
    Log.d("Settings", "==== ================ ====")

    if ((isTiramisuPlus.value && isCantPostNotifications.value) || isNotIgnoreBattery.value) {
        SettingsBaseCard(
            cardTitle = stringResource(R.string.settings_screen_permissions_title),
            cardIconResource = R.drawable.ic_warning,
            cardIconDescription =  stringResource(R.string.settings_screen_up_to_date_icon_description),
            cardColor = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            ),
            cardOnClick = {
                if (isTiramisuPlus.value && isCantPostNotifications.value) {
                    launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
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
            },
        ) {
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = stringResource(R.string.settings_screen_permission_summary),
            )
        }
    } else {
        SettingsBaseCard(
            cardTitle = stringResource(R.string.settings_screen_up_to_date_title),
            cardIconResource = R.drawable.ic_done,
            cardIconDescription =  stringResource(R.string.settings_screen_up_to_date_icon_description),
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