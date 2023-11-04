package com.grigorevmp.habits.presentation.screen.settings

import android.content.Context
import android.os.PowerManager
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SettingsScreenViewModel @Inject constructor() : ViewModel() {

    fun isIgnoringBattery(context: Context): Boolean {
        val pm: PowerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val packageName: String = context.packageName

        return pm.isIgnoringBatteryOptimizations(packageName)
    }

    fun getPackageName(context: Context) = context.packageName
}