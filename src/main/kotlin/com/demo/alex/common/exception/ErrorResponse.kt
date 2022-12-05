package com.example.mark.common.exception

import org.springframework.http.ResponseEntity
import java.lang.Exception

data class ErrorResponse(
    val exceptionClass: String? = null,
    val message: String?
) {
    companion object {
        fun of(e: Exception) = ErrorResponse(
            exceptionClass = e.javaClass.name,
            message = e.message
        )
    }
}

fun ErrorResponse.status(status: Int) =
    ResponseEntity.status(status).body(this)