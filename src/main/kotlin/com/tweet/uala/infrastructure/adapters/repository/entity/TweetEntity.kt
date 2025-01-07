package com.tweet.uala.infrastructure.adapters.repository.entity

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.time.Instant

data class TweetEntity(
    @BsonId var id: String = ObjectId().toString(),
    var userId: String = "",
    var content: String = "",
    var createdAt: Instant = Instant.now()
)
