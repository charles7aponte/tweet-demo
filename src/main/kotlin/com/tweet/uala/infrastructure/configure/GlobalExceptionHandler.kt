package com.tweet.uala.infrastructure.configure

import com.tweet.uala.application.exception.BadRequestException
import com.tweet.uala.application.exception.DuplicateException
import com.tweet.uala.application.exception.ExceptionWrapper
import com.tweet.uala.application.exception.NotFoundException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Error
import io.micronaut.http.server.exceptions.ExceptionHandler
import jakarta.inject.Singleton
@Singleton
class GlobalExceptionHandler : ExceptionHandler<Exception, HttpResponse<Any>> {
    @Error(global = true)
    override fun handle(request: HttpRequest<*>, e: Exception): HttpResponse<Any> {
        return when (e) {
            is BadRequestException, is DuplicateException, is NotFoundException
            -> HttpResponse.badRequest(ExceptionWrapper("bad-request", e.message.toString()))

            else -> HttpResponse.serverError(ExceptionWrapper("internal-error", e.message.toString()))
        }
    }
}
