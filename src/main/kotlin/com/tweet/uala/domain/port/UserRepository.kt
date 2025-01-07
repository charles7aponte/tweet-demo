package com.tweet.uala.domain.port

import com.tweet.uala.domain.model.User

interface UserRepository {
    fun save(user: User): User
    fun findById(id: String): User?
    fun update(user: User): User
    fun saveFollowing(followerId: String, followingId: String): Boolean
}
