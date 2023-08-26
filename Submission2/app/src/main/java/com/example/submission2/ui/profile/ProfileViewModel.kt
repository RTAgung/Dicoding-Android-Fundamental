package com.example.submission2.ui.profile

import androidx.lifecycle.ViewModel
import com.example.submission2.data.AppRepository

class ProfileViewModel(private val appRepository: AppRepository) : ViewModel() {
    var username = "RTAgung"

    fun getDetailUser() = appRepository.getDetailUser(username)
}