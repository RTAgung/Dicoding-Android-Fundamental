package com.example.submission2.data

import com.example.submission2.data.source.datastore.SettingPreferences
import com.example.submission2.data.source.local.service.DbDao
import com.example.submission2.data.source.remote.service.ApiService

class AppRepository private constructor(
    private val apiService: ApiService,
    private val dbDao: DbDao,
    private val settingPreferences: SettingPreferences
) {

    companion object {
        @Volatile
        private var instance: AppRepository? = null
        fun getInstance(
            apiService: ApiService,
            dbDao: DbDao,
            settingPreferences: SettingPreferences
        ): AppRepository =
            instance ?: synchronized(this) {
                instance ?: AppRepository(apiService, dbDao, settingPreferences)
            }.also { instance = it }
    }
}