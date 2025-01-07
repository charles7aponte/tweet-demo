package com.tweet.uala.domain.model

data class User(
    val id: String,
    val name: String,
    val username: String,
    val following: MutableList<String> = mutableListOf()
)
