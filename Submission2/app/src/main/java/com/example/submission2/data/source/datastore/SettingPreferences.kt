package com.example.submission2.data.source.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

class SettingPreferences private constructor(private val dataStore: DataStore<Preferences>) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "setting_datastore")

        @Volatile
        private var INSTANCE: SettingPreferences? = null

        fun getInstance(context: Context): SettingPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingPreferences(context.dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}