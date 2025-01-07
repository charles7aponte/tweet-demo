package com.tweet.uala.infrastructure.api.dto

data class UserResponse(
    val id: String,
    val followings: List<String> = emptyList(),
    val name: String,
    val userName: String
)
