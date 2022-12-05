package com.demo.alex.infra.kafka

import com.demo.alex.common.logger.AppLogger
import com.demo.alex.controller.dto.CommentRequest
import com.demo.alex.controller.event.LikeEvent
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaProducer(
    private val kafkaTemplate: KafkaTemplate<String, Any>,
) {
    fun send(comment: CommentRequest) {
        AppLogger.info("kafka produce", comment.toString())
        kafkaTemplate.send("comment", comment)
    }

    fun send(likeEvent: LikeEvent) {
        AppLogger.info("kafka produce", likeEvent.toString())
        kafkaTemplate.send("like", likeEvent)
    }
}