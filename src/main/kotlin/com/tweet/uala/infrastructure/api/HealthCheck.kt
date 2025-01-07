package com.tweet.uala.infrastructure.api

import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag

@Controller
@ExecuteOn(TaskExecutors.IO)
@Tag(name = "Health")
class HealthCheck {

    @Get("/health")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Health Check", description = "Shows if the microservice is up and running")
    fun getHealthCheck(): HttpResponse<HashMap<String, String>> = HttpResponse.ok(hashMapOf("status" to "UP"))
}
