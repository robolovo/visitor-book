package com.demo.alex.domain

import com.demo.alex.controller.dto.CommentRequest
import com.demo.alex.controller.dto.CommentResponse
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import java.time.ZonedDateTime

@RedisHash("comment")
data class Comment(
    @Id
    val id: String? = null,
    val content: String?,
    var liked: Long = 0,
    val timestamp: ZonedDateTime?
) {
    companion object {
        fun of(dto: CommentRequest): Comment {
            return Comment(content = dto.content, timestamp = dto.timestamp)
        }
    }

    fun toDto(type: String): CommentResponse =
        CommentResponse(this.id!!, this.content, this.liked, this.timestamp, type)
}