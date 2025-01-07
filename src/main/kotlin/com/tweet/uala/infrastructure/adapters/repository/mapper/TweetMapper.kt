package com.tweet.uala.infrastructure.adapters.repository.mapper

import com.tweet.uala.domain.model.Tweet
import com.tweet.uala.infrastructure.adapters.repository.entity.TweetEntity
import jakarta.inject.Singleton

@Singleton
class TweetMapper {

    fun toEntity(tweet: Tweet): TweetEntity =
        TweetEntity(
            id = tweet.id,
            userId = tweet.userId,
            content = tweet.content,
            createdAt = tweet.createdAt
        )

    fun toDomain(tweetEntity: TweetEntity): Tweet =
        Tweet(
            id = tweetEntity.id,
            userId = tweetEntity.userId,
            content = tweetEntity.content,
            createdAt = tweetEntity.createdAt
        )
}
