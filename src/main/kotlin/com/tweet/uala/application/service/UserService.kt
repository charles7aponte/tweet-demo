package com.tweet.uala.application.service

import com.tweet.uala.domain.model.User

interface UserService {
    fun followUser(userFollowerId: String, userFollowingId: String): Boolean
    fun createUser(user: User): User
    fun findOrThrow(userId: String, errorMessage: String): User
}
