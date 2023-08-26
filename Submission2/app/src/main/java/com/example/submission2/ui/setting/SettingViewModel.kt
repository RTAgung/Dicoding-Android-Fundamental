package com.example.submission2.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submission2.data.AppRepository
import kotlinx.coroutines.launch

class SettingViewModel(private val appRepository: AppRepository) : ViewModel() {
    fun getThemeSettings() = appRepository.getThemeSettings()

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            appRepository.saveThemeSetting(isDarkModeActive)
        }
    }
}