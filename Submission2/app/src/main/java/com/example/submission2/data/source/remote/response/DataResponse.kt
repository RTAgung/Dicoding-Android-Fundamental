package com.example.submission2.data.source.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class SearchUserResponse(
    @field:SerializedName("total_count") val totalCount: Int,

    @field:SerializedName("incomplete_results") val incompleteResults: Boolean,

    @field:SerializedName("items") val items: List<UserResponse>
) : Parcelable

@Parcelize
data class UserResponse(
    @field:SerializedName("login") val login: String?,

    @field:SerializedName("id") val id: Int?,

    @field:SerializedName("followers") val followers: Int?,

    @field:SerializedName("avatar_url") val avatarUrl: String?,

    @field:SerializedName("following") val following: Int?,

    @field:SerializedName("public_repos") val publicRepos: Int?,

    @field:SerializedName("name") val name: String?,

    @field:SerializedName("email") val email: String?,

    @field:SerializedName("bio") val bio: String?,
) : Parcelable