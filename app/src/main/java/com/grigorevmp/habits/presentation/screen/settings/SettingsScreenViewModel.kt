package com.grigorevmp.habits.presentation.screen.settings

import android.content.Context
import android.os.PowerManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grigorevmp.habits.data.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    fun isIgnoringBattery(context: Context): Boolean {
        val pm: PowerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val packageName: String = context.packageName

        return pm.isIgnoringBatteryOptimizations(packageName)
    }

    fun getPackageName(context: Context): String = context.packageName

    fun getCongratsEmoji(): Set<String> = preferencesRepository.getCongratsEmoji()

    fun setCongratsEmoji(emoji: Set<String>) = viewModelScope.launch {
        preferencesRepository.setCongratsEmoji(emoji)
    }

    fun getLongerDateFlag(): Boolean = preferencesRepository.getLongerDateFlag()

    fun setLongerDateFlag(makeDayLonger: Boolean) = viewModelScope.launch {
        preferencesRepository.setLongerDateFlag(makeDayLonger)
    }
}