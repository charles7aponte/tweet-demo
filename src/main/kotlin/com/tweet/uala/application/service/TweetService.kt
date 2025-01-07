package com.tweet.uala.application.service

import com.tweet.uala.domain.model.Paginated
import com.tweet.uala.domain.model.Tweet
import java.time.Instant

interface TweetService {
    fun createTweet(userId: String, tweetContent: String): Tweet
    fun findTimelinePaginated(userId: String, startDate: Instant): Paginated<Tweet>
}
