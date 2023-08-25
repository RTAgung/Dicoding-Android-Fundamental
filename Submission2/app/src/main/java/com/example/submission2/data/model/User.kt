package com.example.submission2.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val login: String?,
    val id: Int?,
    val followers: Int?,
    val avatarUrl: String?,
    val following: Int?,
    val repositories: Int?,
    val name: String?,
    val bio: String?,
    val email: String?,
) : Parcelable