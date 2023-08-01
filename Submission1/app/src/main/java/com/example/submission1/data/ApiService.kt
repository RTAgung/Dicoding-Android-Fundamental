package com.example.submission1.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users?")
    fun getSearchUser(
        @Query("q") query: String
    ): Call<SearchUser>

    @GET("users?")
    fun getAllUser(
        @Query("since") page: Int = 1
    ): Call<List<User>>

    @GET("users/{login}")
    fun getDetailUser(
        @Path("login") username: String
    ): Call<User>

    @GET("users/{login}/followers")
    fun getUserFollowers(
        @Path("login") username: String
    ): Call<List<User>>

    @GET("users/{login}/following")
    fun getUserFollowing(
        @Path("login") username: String
    ): Call<List<User>>
}