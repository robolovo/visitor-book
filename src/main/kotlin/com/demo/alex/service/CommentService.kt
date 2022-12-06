package com.demo.alex.service

import com.demo.alex.common.exception.AppException
import com.demo.alex.common.logger.AppLogger
import com.demo.alex.controller.dto.CommentResponse
import com.demo.alex.domain.Comment
import com.demo.alex.domain.CommentRepository
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.repository.findByIdOrNull
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
        val dao = commentRepository.save(comment)
        AppLogger.info("redis publish", dao.toString())
        redisTemplate.convertAndSend("comment", dao.toDto("comment"))
    }

    fun read(): List<CommentResponse> {
        return commentRepository.findAll()
            .map { it.toDto("comment") }
            .sortedBy { it.timestamp }.reversed()
            .toList()
    }

    @Transactional
    fun update(id: String): CommentResponse {
        val comment = commentRepository.findByIdOrNull(id)
            ?: throw AppException.NotFoundException("Not Found")

        comment.liked = comment.liked.plus(1)

        return commentRepository.save(comment)
            .toDto("like")
    }
}