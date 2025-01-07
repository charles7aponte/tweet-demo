package com.tweet.uala.domain.port

interface CacheRepository {

    fun <T> get(key: String, clazz: Class<T>): T?
    fun delete(key: String): Long?
    fun <T> remember(seconds: Long, key: String, clazz: Class<T>, execute: () -> T?): T?
    fun <T> set(key: String, data: T, seconds: Long)
}
