package com.tweet.uala.domain.model

import java.time.Instant

data class Paginated<T>(
    val items: List<T>,
    val nextPageDate: Instant?,
    val isLastPage: Boolean
)
