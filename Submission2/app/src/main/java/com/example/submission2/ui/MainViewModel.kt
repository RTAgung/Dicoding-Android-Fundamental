package com.example.submission2.ui

import androidx.lifecycle.ViewModel
import com.example.submission2.data.AppRepository

class MainViewModel(private val appRepository: AppRepository) : ViewModel() {
    fun getThemeSettings() = appRepository.getThemeSettings()
}