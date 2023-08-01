package com.example.submission2.data.source.remote.service

import com.example.submission2.data.source.remote.response.SearchUserResponse
import com.example.submission2.data.source.remote.response.UserResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users?")
    suspend fun getSearchUser(
        @Query("q") query: String
    ): SearchUserResponse

    @GET("users?")
    suspend fun getAllUser(
        @Query("since") page: Int = 1
    ): List<UserResponse>

    @GET("users/{login}")
    suspend fun getDetailUser(
        @Path("login") username: String
    ): UserResponse

    @GET("users/{login}/followers")
    suspend fun getUserFollowers(
        @Path("login") username: String
    ): List<UserResponse>

    @GET("users/{login}/following")
    suspend fun getUserFollowing(
        @Path("login") username: String
    ): List<UserResponse>
}