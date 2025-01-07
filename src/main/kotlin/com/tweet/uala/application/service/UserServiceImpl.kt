package com.tweet.uala.application.service

import com.tweet.uala.application.const.RedisKeys
import com.tweet.uala.application.exception.BadRequestException
import com.tweet.uala.application.exception.NotFoundException
import com.tweet.uala.domain.model.User
import com.tweet.uala.domain.port.CacheRepository
import com.tweet.uala.domain.port.UserRepository
import jakarta.inject.Singleton

@Singleton
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val cacheRepository: CacheRepository
) : UserService {

    private fun getKeyUser(userId: String): String =
        "${RedisKeys.user.key}.$userId"

    override fun findOrThrow(userId: String, errorMessage: String): User {
        return cacheRepository.remember(
            RedisKeys.user.ttlSeg,
            getKeyUser(userId),
            User::class.java
        ) {
            userRepository.findById(userId)
        } ?: throw NotFoundException(errorMessage)
    }

    override fun createUser(user: User): User {
        return userRepository.save(user).also { savedUser ->
            cacheRepository.set(getKeyUser(savedUser.id), savedUser, RedisKeys.user.ttlSeg)
        }
    }

    override fun followUser(userFollowerId: String, userFollowingId: String): Boolean {
        val follower = findOrThrow(userFollowerId, "Follower user '$userFollowerId' not found")
        val following = findOrThrow(userFollowingId, "Following user '$userFollowingId' not found")

        if (!userRepository.saveFollowing(follower.id, following.id)) {
            throw BadRequestException("User '$userFollowerId' is already being followed by '$userFollowingId'.")
        }

        follower.following.add(userFollowingId)
        cacheRepository.delete(getKeyUser(follower.id))
        cacheRepository.set(getKeyUser(follower.id), follower, RedisKeys.user.ttlSeg)

        return true
    }
}
