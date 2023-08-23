package com.example.submission2.utils

import com.example.submission2.data.model.User
import com.example.submission2.data.source.local.entity.UserEntity
import com.example.submission2.data.source.remote.response.UserResponse

object Mapping {
    fun listUserEntityToListUser(listUserEntity: List<UserEntity>): List<User> {
        val listUser = ArrayList<User>()
        for (userEntity in listUserEntity) {
            listUser.add(
                User(
                    id = userEntity.id,
                    avatarUrl = userEntity.avatarUrl,
                    login = userEntity.login,
                    name = "null",
                    following = 0,
                    followers = 0
                )
            )
        }
        return listUser
    }

    fun listUserResponseToListUserEntity(listUserResponse: List<UserResponse>): List<UserEntity> {
        val listUserEntity = ArrayList<UserEntity>()
        for (userResponse in listUserResponse) {
            listUserEntity.add(
                UserEntity(
                    id = userResponse.id ?: 0,
                    avatarUrl = userResponse.avatarUrl ?: "null",
                    login = userResponse.login ?: "null"
                )
            )
        }
        return listUserEntity
    }

    fun listUserResponseToListUser(listUserResponse: List<UserResponse>): List<User> {
        val listUser = ArrayList<User>()
        for (userResponse in listUserResponse) {
            listUser.add(
                User(
                    id = userResponse.id ?: 0,
                    avatarUrl = userResponse.avatarUrl ?: "null",
                    login = userResponse.login ?: "null",
                    name = userResponse.name ?: "null",
                    following = userResponse.following ?: 0,
                    followers = userResponse.followers ?: 0
                )
            )
        }
        return listUser
    }
}