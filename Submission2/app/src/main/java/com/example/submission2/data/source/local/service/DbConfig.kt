package com.example.submission2.data.source.local.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.submission2.data.source.local.entity.FavoriteUserEntity
import com.example.submission2.data.source.local.entity.UserEntity

@Database(
    entities = [FavoriteUserEntity::class, UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class DbConfig : RoomDatabase() {
    abstract fun dbDao(): DbDao

    companion object {
        @Volatile
        private var instance: DbConfig? = null
        fun getInstance(context: Context): DbConfig =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    DbConfig::class.java, "GithubUser.db"
                ).build()
            }
    }
}