package com.demo.alex.controller.dto

import java.time.ZonedDateTime

data class CommentRequest(
    val content: String?,
    val timestamp: ZonedDateTime?,
    val type: String
)
