package com.tweet.uala.application.service

import com.tweet.uala.application.exception.BadRequestException
import com.tweet.uala.application.exception.NotFoundException
import com.tweet.uala.domain.model.Paginated
import com.tweet.uala.domain.model.Tweet
import com.tweet.uala.domain.model.User
import com.tweet.uala.domain.port.TweetRepository
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Instant

@MicronautTest
class TweetServiceImplTest {
    private val userId = "123"

    private val userMock =
        User(id = userId, name = "John", username = "john_doe", following = mutableListOf("456", "789"))

    @MockBean(TweetRepository::class)
    fun tweetRepository(): TweetRepository = mockk<TweetRepository>()

    @Inject
    private lateinit var tweetRepository: TweetRepository

    @MockBean(UserService::class)
    fun userRepository(): UserService = mockk<UserService>()

    @Inject
    private lateinit var userService: UserService

    @Inject
    private lateinit var tweetService: TweetService

    @Test
    fun `createTweet should throw BadRequestException if tweet content exceeds the limit`() {
        val tweetContentExceedingLimit = "a".repeat(281)

        val exception = assertThrows<BadRequestException> {
            tweetService.createTweet(userId, tweetContentExceedingLimit)
        }

        Assertions.assertEquals("tweet limit is 280", exception.message)
    }

    @Test
    fun `createTweet should create a tweet if content is within the limit`() {
        val tweetContent = "este es mi tweet"
        val tweet = Tweet(userId = userId, content = tweetContent)
        every { userService.findOrThrow(userId, "User not found") } returns userMock
        every { tweetRepository.save(any()) } returns tweet

        val result = tweetService.createTweet(userId, tweetContent)

        Assertions.assertEquals(userId, result.userId)
        Assertions.assertEquals(tweetContent, result.content)
        verify(exactly = 1) { userService.findOrThrow(userId, "User not found") }
        verify(exactly = 1) { tweetRepository.save(any()) }
    }

    @Test
    fun `findTimelinePaginated should return tweets for the user's following`() {
        val startDate = Instant.now()
        val paginatedTweets = Paginated(listOf(Tweet(userId = "456", content = "First tweet")), null, false)
        every { userService.findOrThrow(userId, "User not found") } returns userMock
        every { tweetRepository.findTimelinePaginated(userMock.following, startDate) } returns paginatedTweets

        val result = tweetService.findTimelinePaginated(userId, startDate)

        Assertions.assertNotNull(result)
        Assertions.assertEquals(1, result.items.size)
        Assertions.assertEquals("456", result.items.first().userId)
        verify(exactly = 1) { userService.findOrThrow(userId, "User not found") }
        verify(exactly = 1) { tweetRepository.findTimelinePaginated(userMock.following, startDate) }
    }

    @Test
    fun `findTimelinePaginated should throw NotFoundException if user does not exist`() {
        val startDate = Instant.now()
        every { userService.findOrThrow(userId, "User not found") } throws NotFoundException("User not found")

        val exception = assertThrows<NotFoundException> {
            tweetService.findTimelinePaginated(userId, startDate)
        }
        Assertions.assertEquals("User not found", exception.message)
    }
}
