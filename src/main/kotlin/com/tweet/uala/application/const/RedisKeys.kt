package com.tweet.uala.application.const

object RedisKeys {
    val user = KeyConfig(key = "user.all", ttlSeg = 36000)
}

data class KeyConfig(
    val key: String,
    val ttlSeg: Long
)
