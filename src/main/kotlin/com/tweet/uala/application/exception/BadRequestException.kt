package com.tweet.uala.application.exception

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties("stackTrace", "localizedMessage")
class BadRequestException(message: String = "Bad Request") : RuntimeException(message)
