package com.tweet.uala.infrastructure.api.mapper

import com.tweet.uala.domain.model.Tweet
import com.tweet.uala.infrastructure.api.dto.TweetResponse
import jakarta.inject.Singleton

@Singleton
class TweetDtoMapper {

    fun toResponse(tweet: Tweet): TweetResponse {
        return TweetResponse(
            id = tweet.id,
            userId = tweet.userId,
            content = tweet.content,
            createdAt = tweet.createdAt
        )
    }
}
