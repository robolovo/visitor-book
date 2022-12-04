package com.demo.alex.infra.kafka

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
    private val mapper: ObjectMapper
) {

    @KafkaListener(
        topics = ["comment"],
        groupId = "alex",
        containerFactory = "kafkaListenerContainerFactoryComment"
    )
    fun consume(message: CommentRequest) {
        println("kafka consumer")
        println(message.toString())
        commentService.save(Comment.of(message))
    }

    @KafkaListener(
        topics = ["like"],
        groupId = "alex",
        containerFactory = "kafkaListenerContainerFactoryLikeEvent"
    )
    fun consumer(likeEvent: LikeEvent) {
        println("kafka consumer")
        println(likeEvent.toString())
        val comment = commentService.update(likeEvent.id)
        redisTemplate.convertAndSend("comment", comment)
    }
}