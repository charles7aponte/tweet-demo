package com.tweet.uala.infrastructure.adapters.repository.entity

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class UserEntity(
    @BsonId var id: String = ObjectId().toString(),
    var name: String = "",
    var username: String = "",
    var following: MutableList<String> = mutableListOf()
)
