package com.tweet.uala.application.service

import com.tweet.uala.application.const.RedisKeys
import com.tweet.uala.domain.model.User
import com.tweet.uala.domain.port.CacheRepository
import com.tweet.uala.domain.port.UserRepository
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@MicronautTest
class UserServiceImplTest {

    private val userId = "123"
    private val user = User(userId, "name test", "User Name", mutableListOf())

    @MockBean(UserRepository::class)
    fun userRepository(): UserRepository = mockk<UserRepository>()

    @Inject
    private lateinit var userRepository: UserRepository

    @Inject
    private lateinit var cacheRepository: CacheRepository

    @Inject
    private lateinit var userService: UserService

    private fun getKeyUser(userId: String): String =
        "${RedisKeys.user.key}.$userId"

    @Test
    fun `createUser should save user and cache it`() {
        every { userRepository.save(user) } returns user

        val result = userService.createUser(user)

        verify(exactly = 1) { userRepository.save(user) }
        assert(result == user)
        Assertions.assertEquals(userId, result.id)
    }

    @Test
    fun `findOrThrow - should throw NotFoundException if user is not found`() {
        val userId = "00000user"
        cacheRepository.delete(getKeyUser(userId))

        val exception = assertThrows<com.tweet.uala.application.exception.NotFoundException> {
            userService.findOrThrow(userId, "User not found")
        }
        Assertions.assertEquals("User not found", exception.message)
    }

    @Test
    fun `followUser - should follow another user successfully`() {
        val userFollowerId = "follower123"
        val userFollowingId = "following123"
        val follower = User(id = userFollowerId, name = "John", username = "john_doe", following = mutableListOf())
        val following = User(id = userFollowingId, name = "Doe", username = "doe_john")

        every { userRepository.findById(userFollowerId) } returns follower
        every { userRepository.findById(userFollowingId) } returns following
        every { userRepository.saveFollowing(follower.id, following.id) } returns true
        cacheRepository.delete(getKeyUser(userFollowerId))
        cacheRepository.delete(getKeyUser(userFollowingId))

        val result = userService.followUser(userFollowerId, userFollowingId)

        Assertions.assertTrue(result)
        Assertions.assertTrue(follower.following.contains(userFollowingId))
        verify(exactly = 1) { userRepository.findById(userFollowerId) }
        verify(exactly = 1) { userRepository.findById(userFollowingId) }
        verify(exactly = 1) { userRepository.saveFollowing(follower.id, following.id) }
    }
}
