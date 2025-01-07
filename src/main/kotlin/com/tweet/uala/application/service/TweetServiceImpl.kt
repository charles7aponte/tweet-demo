package com.tweet.uala.application.service

import com.tweet.uala.application.exception.BadRequestException
import com.tweet.uala.domain.model.Paginated
import com.tweet.uala.domain.model.Tweet
import com.tweet.uala.domain.port.TweetRepository
import jakarta.inject.Singleton
import java.time.Instant

@Singleton
class TweetServiceImpl(
    private val tweetRepository: TweetRepository,
    private val userService: UserService
) : TweetService {

    companion object {
        const val LIMIT_TWEET = 280
    }

    override fun createTweet(userId: String, tweetContent: String): Tweet {
        if (tweetContent.length > LIMIT_TWEET) {
            throw BadRequestException("tweet limit is $LIMIT_TWEET")
        }

        userService.findOrThrow(userId, "User not found")

        val tweet = Tweet(userId = userId, content = tweetContent)

        return tweetRepository.save(tweet)
    }

    override fun findTimelinePaginated(userId: String, startDate: Instant): Paginated<Tweet> {
        val user = userService.findOrThrow(userId, "User not found")

        return tweetRepository.findTimelinePaginated(user.following, startDate)
    }
}
