package com.example.submission2.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submission2.data.AppRepository
import com.example.submission2.data.model.User
import kotlinx.coroutines.launch

class DetailViewModel(private val appRepository: AppRepository) : ViewModel() {
    var username = ""
    var isFavoriteState = false
    private var isFavoriteLive = MutableLiveData<Boolean>()
    var data: User? = null

    init {
        setFavorite(false)
    }

    fun setFavorite(isFavorite: Boolean) {
        this.isFavoriteLive.value = isFavorite
    }

    fun getFavorite() = isFavoriteLive

    fun getDetailUser() = appRepository.getDetailUser(username)

    fun checkFavorite() = appRepository.isFavorite(username)

    fun insertFavorite() {
        viewModelScope.launch {
            data?.let { appRepository.insertFavorite(it) }
        }
    }

    fun deleteFavorite() {
        viewModelScope.launch {
            appRepository.deleteFavorite(username)
        }
    }
}