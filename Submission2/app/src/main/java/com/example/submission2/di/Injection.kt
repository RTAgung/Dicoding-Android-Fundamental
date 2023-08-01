package com.example.submission2.di

import android.content.Context
import com.example.submission2.data.AppRepository
import com.example.submission2.data.source.datastore.SettingPreferences
import com.example.submission2.data.source.local.service.DbConfig
import com.example.submission2.data.source.remote.service.ApiConfig

object Injection {
    fun provideRepository(context: Context): AppRepository {
        val apiService = ApiConfig.getApiService()
        val dbDao = DbConfig.getInstance(context).dbDao()
        val appPreferences = SettingPreferences.getInstance(context)
        return AppRepository.getInstance(apiService, dbDao, appPreferences)
    }
}