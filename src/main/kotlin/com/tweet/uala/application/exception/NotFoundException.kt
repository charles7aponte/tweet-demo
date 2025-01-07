package com.tweet.uala.application.exception

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties("stackTrace", "localizedMessage")
class NotFoundException(message: String = "Not Found") : RuntimeException(message)
