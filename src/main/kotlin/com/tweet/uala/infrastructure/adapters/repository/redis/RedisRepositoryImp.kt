package com.tweet.uala.infrastructure.adapters.repository.redis

import com.tweet.uala.domain.port.CacheRepository
import com.tweet.uala.utils.jsonToObject
import com.tweet.uala.utils.mapper
import io.lettuce.core.api.StatefulRedisConnection
import io.micronaut.retry.annotation.CircuitBreaker
import io.micronaut.retry.annotation.Recoverable
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory

@Singleton
@Recoverable(api = RedisRepositoryFallback::class)
@SuppressWarnings("TooGenericExceptionCaught")
@CircuitBreaker(delay = "1ms", attempts = "2", reset = "2s")
class RedisRepositoryImp(
    private val redis: StatefulRedisConnection<String, String>
) : CacheRepository {

    private val logger = LoggerFactory.getLogger(RedisRepositoryImp::class.java)

    companion object {
        const val KEY_LOGGER = "REDIS_IMP"
        const val KEY_REDIS = "tweet_uala::"
    }

    private val syncConnection
        get() = redis.sync()

    override fun <T> set(key: String, data: T, seconds: Long) {
        executeWithLogging("$KEY_LOGGER-redis-set-[$key]") {
            val redisKey = formatKey(key)
            val value = mapper.writeValueAsString(data)
            syncConnection.setex(redisKey, seconds, value)
        }
    }

    override fun <T> get(key: String, clazz: Class<T>): T? {
        return executeWithLogging("$KEY_LOGGER-redis-get-[$key]") {
            val redisKey = formatKey(key)
            syncConnection.get(redisKey)?.jsonToObject(clazz)
        }
    }

    override fun delete(key: String): Long? {
        return executeWithLogging("$KEY_LOGGER-redis-delete-[$key]") {
            val redisKey = formatKey(key)
            syncConnection.del(redisKey)
        }
    }

    override fun <T> remember(seconds: Long, key: String, clazz: Class<T>, execute: () -> T?): T? {
        return executeWithLogging("$KEY_LOGGER-redis-remember-[$key]") {
            get(key, clazz) ?: execute()?.also {
                set(key, it, seconds)
            }
        }
    }

    private fun formatKey(key: String): String = "$KEY_REDIS$key"

    private fun <T> executeWithLogging(logMessage: String, action: () -> T?): T? {
        return runCatching {
            action()
        }.onFailure { e ->
            logger.error(logMessage, e)
        }.getOrNull()
    }
}
