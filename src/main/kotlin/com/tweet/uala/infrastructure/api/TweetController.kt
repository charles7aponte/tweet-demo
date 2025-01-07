package com.tweet.uala.infrastructure.api

import com.tweet.uala.application.service.TweetService
import com.tweet.uala.domain.model.Paginated
import com.tweet.uala.domain.model.Tweet
import com.tweet.uala.infrastructure.api.dto.CreateTweetRequest
import com.tweet.uala.infrastructure.api.dto.TweetResponse
import com.tweet.uala.infrastructure.api.mapper.TweetDtoMapper
import com.tweet.uala.infrastructure.cons.Headers
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.QueryValue
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import java.time.Instant

@Controller("/tweet")
@Tag(name = "Tweet Controller", description = "Controller for managing tweets")
class TweetController(
    private val tweetService: TweetService,
    private val tweetDtoMapper: TweetDtoMapper
) {

    @Post
    fun create(
        @Header(Headers.X_USER_ID) userId: String,
        @Body createTweetRequest: CreateTweetRequest
    ): TweetResponse {
        return tweetService.createTweet(userId, createTweetRequest.content)
            .let(tweetDtoMapper::toResponse)
    }

    @Operation(
        summary = "Get all tweets",
        description = "Retrieve all tweets from the system"
    )
    @Get("/time-line/by/week")
    fun getTimeline(
        @Header(Headers.X_USER_ID) userId: String,
        @QueryValue(value = "start_date") startDate: Instant
    ): Paginated<Tweet> {
        return tweetService.findTimelinePaginated(userId, startDate)
    }
}
