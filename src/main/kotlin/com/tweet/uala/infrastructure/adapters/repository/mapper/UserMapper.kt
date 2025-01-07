package com.tweet.uala.infrastructure.adapters.repository.mapper

import com.tweet.uala.infrastructure.adapters.repository.entity.UserEntity
import jakarta.inject.Singleton
import org.bson.types.ObjectId

@Singleton
class UserMapper {

    fun toEntity(user: com.tweet.uala.domain.model.User): UserEntity =
        UserEntity(
            id = user.id ?: ObjectId().toString(),
            name = user.name,
            username = user.username,
            following = user.following
        )

    fun toDomain(userEntity: UserEntity): com.tweet.uala.domain.model.User =
        com.tweet.uala.domain.model.User(
            id = userEntity.id,
            name = userEntity.name,
            username = userEntity.username,
            following = userEntity.following
        )
}
