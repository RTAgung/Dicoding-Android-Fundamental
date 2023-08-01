package com.example.submission2.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
class UserEntity(
    @ColumnInfo(name = "login")
    @PrimaryKey
    val login: String,

    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String,
)