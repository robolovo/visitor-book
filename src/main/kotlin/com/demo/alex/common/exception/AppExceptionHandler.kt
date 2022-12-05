package com.demo.alex.common.exception

import com.example.mark.common.exception.ErrorResponse
import com.example.mark.common.exception.status
import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class AppExceptionHandler {

    val logger = KotlinLogging.logger {}

    @ExceptionHandler(Exception::class)
    fun handleExceptions(e: Exception): ResponseEntity<ErrorResponse> {
        logger.error(e.javaClass.name, e)

        return when (e::class) {
            AppException.NotFoundException::class -> ErrorResponse.of(e).status(404)
            else -> ErrorResponse.of(e).status(500)
        }
    }
}