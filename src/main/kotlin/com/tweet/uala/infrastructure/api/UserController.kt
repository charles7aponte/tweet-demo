package com.tweet.uala.infrastructure.api

import com.tweet.uala.application.service.UserService
import com.tweet.uala.infrastructure.api.dto.CreateUserRequest
import com.tweet.uala.infrastructure.api.dto.UserResponse
import com.tweet.uala.infrastructure.api.mapper.UserDtoMapper
import com.tweet.uala.infrastructure.cons.Headers
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Header
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post

@Controller("/users")
class UserController(
    private val userService: UserService,
    private val userDtoMapper: UserDtoMapper
) {

    @Post
    fun createUser(
        @Body createUserRequest: CreateUserRequest
    ): UserResponse {
        return userService.createUser(createUserRequest.toDomain())
            .let(userDtoMapper::toResponse)
    }

    @Post("/follow/{followingId}")
    fun followUser(
        @Header(Headers.X_USER_ID) userId: String,
        @PathVariable followingId: String
    ): Map<String, String> {
        userService.followUser(userId, followingId)
        return mapOf("message" to "User '$userId' is now following '$followingId'")
    }
}
