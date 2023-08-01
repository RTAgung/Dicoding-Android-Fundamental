package com.dicoding.newsapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.newsapp.data.NewsRepository
import com.dicoding.newsapp.data.local.entity.NewsEntity
import kotlinx.coroutines.launch

class NewsViewModel(private val newsRepository: NewsRepository) : ViewModel() {
    fun getHeadlineNews() = newsRepository.getHeadlineNews()

    fun getBookmarkedNews() = newsRepository.getBookmarkedNews()

    suspend fun saveNews(news: NewsEntity) {
        viewModelScope.launch{
            newsRepository.setBookmarkedNews(news, true)
        }
    }

    suspend fun deleteNews(news: NewsEntity) {
        viewModelScope.launch {
            newsRepository.setBookmarkedNews(news, false)
        }
    }
}
