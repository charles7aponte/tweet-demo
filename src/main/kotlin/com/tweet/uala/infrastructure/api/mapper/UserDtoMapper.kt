package com.tweet.uala.infrastructure.api.mapper

import com.tweet.uala.domain.model.User
import com.tweet.uala.infrastructure.api.dto.UserResponse
import jakarta.inject.Singleton

@Singleton
class UserDtoMapper {

    fun toResponse(user: User): UserResponse {
        return UserResponse(
            id = user.id ?: "",
            followings = user.following,
            name = user.name,
            userName = user.username
        )
    }
}
