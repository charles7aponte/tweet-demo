package com.tweet.uala.domain.port

import com.tweet.uala.domain.model.Paginated
import com.tweet.uala.domain.model.Tweet
import java.time.Instant

interface TweetRepository {

    fun save(tweet: Tweet): Tweet

    fun findByUserIds(userIds: List<String>): List<Tweet>

    fun findTimelinePaginated(
        userIds: List<String>,
        currentDate: Instant
    ): Paginated<Tweet>
}
