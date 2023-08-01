package com.example.submission2.data.source.local.service

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.submission2.data.source.local.entity.FavoriteUserEntity
import com.example.submission2.data.source.local.entity.UserEntity

@Dao
interface DbDao {
    //    Query For All User Data
    @Query("SELECT * FROM user")
    fun getAllUser(): LiveData<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(users: List<UserEntity>)

    @Query("DELETE FROM user")
    suspend fun deleteUser()

    //    Query For Only Favorite User
    @Query("SELECT * FROM favorite")
    fun getAllFavorite(): LiveData<List<FavoriteUserEntity>>

    @Query("SELECT EXISTS(SELECT * FROM favorite WHERE login = :userLogin)")
    suspend fun isFavorite(userLogin: String): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavorite(favoriteUser: FavoriteUserEntity)

    @Query("DELETE FROM favorite WHERE login = :userLogin")
    suspend fun deleteFavorite(userLogin: String)
}