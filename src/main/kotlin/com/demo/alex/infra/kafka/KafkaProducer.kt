package com.demo.alex.infra.kafka

import com.demo.alex.controller.dto.CommentRequest
import com.demo.alex.controller.event.LikeEvent
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaProducer(
    private val kafkaTemplate: KafkaTemplate<String, Any>,
    private val mapper: ObjectMapper
) {
    fun send(comment: CommentRequest) {
        println("kafka produce")
        println(comment.toString())
        kafkaTemplate.send("comment", comment)
    }

    fun send(likeEvent: LikeEvent) {
        println("kafka produce")
        println(likeEvent.toString())
        kafkaTemplate.send("like", likeEvent)
    }
}