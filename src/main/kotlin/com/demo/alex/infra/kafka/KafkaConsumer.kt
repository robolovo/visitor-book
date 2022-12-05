package com.demo.alex.infra.kafka

import com.demo.alex.common.logger.AppLogger
import com.demo.alex.controller.dto.CommentRequest
import com.demo.alex.controller.event.LikeEvent
import com.demo.alex.domain.Comment
import com.demo.alex.service.CommentService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class KafkaConsumer(
    private val commentService: CommentService,
    private val redisTemplate: RedisTemplate<String, String>,
) {

    @KafkaListener(
        topics = ["comment"],
        groupId = "alex",
        containerFactory = "kafkaListenerContainerFactoryComment"
    )
    fun consume(message: CommentRequest) {
        AppLogger.info("kafka consumer", message.toString())
        commentService.save(Comment.of(message))
    }

    @KafkaListener(
        topics = ["like"],
        groupId = "alex",
        containerFactory = "kafkaListenerContainerFactoryLikeEvent"
    )
    fun consumer(likeEvent: LikeEvent) {
        AppLogger.info("kafka consumer", likeEvent.toString())
        val comment = commentService.update(likeEvent.id)
        redisTemplate.convertAndSend("comment", comment)
    }
}