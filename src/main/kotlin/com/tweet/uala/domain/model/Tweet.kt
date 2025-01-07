package com.tweet.uala.domain.model

import java.time.Instant
import java.util.UUID

data class Tweet(
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val content: String,
    val createdAt: Instant = Instant.now()
)
