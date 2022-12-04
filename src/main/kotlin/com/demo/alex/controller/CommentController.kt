package com.demo.alex.controller

import com.demo.alex.controller.dto.CommentRequest
import com.demo.alex.controller.dto.CommentResponse
import com.demo.alex.controller.event.LikeEvent
import com.demo.alex.infra.kafka.KafkaProducer
import com.demo.alex.service.CommentService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(
    "/api/comments",
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
class CommentController(
    private val kafkaProducer: KafkaProducer,
    private val commentService: CommentService,
) {
    @MessageMapping("/comments")
    fun processMessage(@Payload comment: CommentRequest) =
        kafkaProducer.send(comment)

    @MessageMapping("/likes")
    fun processLike(@Payload likeEvent: LikeEvent) =
        kafkaProducer.send(likeEvent)

    @GetMapping("")
    fun readMessages(): ResponseEntity<List<CommentResponse>> =
        ResponseEntity.ok().body(commentService.read())
}