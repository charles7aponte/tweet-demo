package com.tweet.uala.infrastructure.adapters.repository.redis

import com.tweet.uala.domain.port.CacheRepository
import io.micronaut.retry.annotation.Fallback

@Fallback
@SuppressWarnings("EmptyFunctionBlock")
class RedisRepositoryFallback : CacheRepository {

    override fun <T> set(key: String, data: T, seconds: Long) {}

    override fun <T> get(key: String, clazz: Class<T>): T? {
        return null
    }

    override fun delete(key: String): Long {
        return 0L
    }

    override fun <T> remember(seconds: Long, key: String, clazz: Class<T>, execute: () -> T?): T? {
        return execute()
    }
}
