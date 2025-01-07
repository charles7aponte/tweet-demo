package com.tweet.uala.application.exception

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties("stackTrace", "localizedMessage")
class DuplicateException(message: String = "Duplicate Info") : RuntimeException(message)
