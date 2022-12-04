package com.demo.alex.service

import com.demo.alex.controller.dto.CommentResponse
import com.demo.alex.domain.Comment
import com.demo.alex.domain.CommentRepository
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional(readOnly = true)
class CommentService(
    private val commentRepository: CommentRepository,
    private val redisTemplate: RedisTemplate<String, String>
) {

    @Transactional
    fun save(comment: Comment) {
        println("redis publish")
        val savedComment = commentRepository.save(comment)
        println(comment.toString())
        redisTemplate.convertAndSend("comment", savedComment.toDto())
    }

    fun read(): List<CommentResponse> {
        return commentRepository.findAll()
            .map { it.toDto() }
            .sortedBy { it.timestamp }.reversed()
            .toList()
    }

    fun update(id: String): CommentResponse {
        val comment = commentRepository.findById(id).get()
        comment.liked = comment.liked.plus(1)
        return commentRepository.save(comment)
            .toDto()
    }
}