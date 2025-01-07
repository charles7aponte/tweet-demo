package com.tweet.uala.infrastructure.api.dto

import java.time.Instant

class TweetResponse(
    val id: String,
    val userId: String,
    val content: String,
    val createdAt: Instant
)
