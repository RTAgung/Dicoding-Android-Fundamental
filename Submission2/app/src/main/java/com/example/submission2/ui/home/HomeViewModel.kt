package com.example.submission2.ui.home

import androidx.lifecycle.ViewModel
import com.example.submission2.data.AppRepository

class HomeViewModel(private val appRepository: AppRepository) : ViewModel() {
    fun getAllUser() = appRepository.getAllUser()

    fun getSearchUser(query: String) = appRepository.getSearchUser(query)
}