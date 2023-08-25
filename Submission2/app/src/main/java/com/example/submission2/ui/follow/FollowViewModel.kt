package com.example.submission2.ui.follow

import androidx.lifecycle.ViewModel
import com.example.submission2.data.AppRepository

class FollowViewModel(private val appRepository: AppRepository) : ViewModel() {
    fun getUserFollowers(username: String) = appRepository.getUserFollowers(username)
    fun getUserFollowing(username: String) = appRepository.getUserFollowing(username)
}