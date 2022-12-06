package com.demo.alex.controller.dto

import java.time.ZonedDateTime

data class CommentResponse(
    val id: String,
    val content: String?,
    val liked: Long,
    val timestamp: ZonedDateTime?,
    val type: String
)