package com.tweet.uala.infrastructure.api.dto

import org.bson.types.ObjectId

data class CreateUserRequest(
    val name: String,
    val userName: String
) {
    fun toDomain(): com.tweet.uala.domain.model.User {
        return com.tweet.uala.domain.model.User(
            id = ObjectId().toString(),
            name = this.name,
            username = this.userName
        )
    }
}
