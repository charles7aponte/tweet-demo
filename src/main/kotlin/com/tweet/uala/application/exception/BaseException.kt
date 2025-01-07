package com.tweet.uala.application.exception

import io.micronaut.http.HttpStatus

abstract class BaseException(
    val errorCode: String,
    val errorMessage: String,
    val errorStatus: HttpStatus
) : RuntimeException(errorMessage)
