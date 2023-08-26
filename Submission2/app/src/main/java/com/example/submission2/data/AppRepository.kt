package com.example.submission2.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.submission2.data.model.User
import com.example.submission2.data.source.datastore.SettingPreferences
import com.example.submission2.data.source.local.service.DbDao
import com.example.submission2.data.source.remote.service.ApiService
import com.example.submission2.utils.Mapping

class AppRepository private constructor(
    private val apiService: ApiService,
    private val dbDao: DbDao,
    private val settingPreferences: SettingPreferences
) {

    fun getAllUser(): LiveData<Result<List<User>>> = liveData {
        emit(Result.Loading)
        try {
            val random = (1..100000).random()
            val response = apiService.getAllUser(random)
            val userEntity = Mapping.listUserResponseToListUserEntity(response)
            dbDao.deleteUser()
            dbDao.insertUser(userEntity)
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
        val data: LiveData<Result<List<User>>> =
            dbDao.getAllUser().map { Result.Success(Mapping.listUserEntityToListUser(it)) }
        emitSource(data)
    }

    fun getSearchUser(query: String): LiveData<Result<List<User>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getSearchUser(query)
            val listUserResponse = response.items
            val listUser = Mapping.listUserResponseToListUser(listUserResponse)
            emit(Result.Success(listUser))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getDetailUser(username: String): LiveData<Result<User>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getDetailUser(username)
            val user = Mapping.userResponseToUser(response)
            emit(Result.Success(user))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getUserFollowers(username: String): LiveData<Result<List<User>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getUserFollowers(username)
            val listUser = Mapping.listUserResponseToListUser(response)
            emit(Result.Success(listUser))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getUserFollowing(username: String): LiveData<Result<List<User>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getUserFollowing(username)
            val listUser = Mapping.listUserResponseToListUser(response)
            emit(Result.Success(listUser))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getAllFavorite(): LiveData<List<User>> = liveData {
        val favoriteUserEntity = dbDao.getAllFavorite()
        val user = Mapping.listFavoriteUserEntityToListUser(favoriteUserEntity)
        emit(user)
    }

    fun isFavorite(username: String): LiveData<Boolean> = liveData {
        emit(dbDao.isFavorite(username))
    }

    suspend fun insertFavorite(user: User) {
        val favoriteUserEntity = Mapping.userToFavoriteUserEntity(user)
        dbDao.insertFavorite(favoriteUserEntity)
    }

    suspend fun deleteFavorite(username: String) {
        dbDao.deleteFavorite(username)
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return settingPreferences.getThemeSetting().asLiveData()
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        settingPreferences.saveThemeSetting(isDarkModeActive)
    }

    companion object {
        @Volatile
        private var instance: AppRepository? = null
        fun getInstance(
            apiService: ApiService, dbDao: DbDao, settingPreferences: SettingPreferences
        ): AppRepository = instance ?: synchronized(this) {
            instance ?: AppRepository(apiService, dbDao, settingPreferences)
        }.also { instance = it }
    }
}