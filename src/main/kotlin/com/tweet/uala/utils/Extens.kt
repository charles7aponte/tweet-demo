package com.tweet.uala.utils

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature

val mapper: ObjectMapper = ObjectMapper()
    .findAndRegisterModules()
    .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
    .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)

fun Any.toJsonString(): String = mapper.writeValueAsString(this)

fun <T> String.jsonToObject(bodyObject: Class<T>): T? =
    try { mapper.readValue(this, bodyObject) } catch (_: Exception) { null }
