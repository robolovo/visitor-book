package com.demo.alex.infra.redis

import com.demo.alex.common.logger.AppLogger
import com.demo.alex.controller.dto.CommentResponse
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

@Component
class DefaultMessageDelegate(
    private val messagingTemplate: SimpMessagingTemplate,
    private val mapper: ObjectMapper
) : MessageDelegate {

    override fun handleMessage(message: String) {
        AppLogger.info("redis subscribe", message)

        val comment = mapper.readValue(message, CommentResponse::class.java)

        when (comment.type) {
            "comment" -> {
                messagingTemplate.convertAndSend("/comment", comment)
            }
            "like" -> {
                println("!!!!!!!!!!!!!!!!!!!!!!!!!!!")
                println("!!!!!!!!!!!!!!!!!!!!!!!!!!!")
                println("!!!!!!!!!!!!!!!!!!!!!!!!!!!")
                println(comment.toString())
                messagingTemplate.convertAndSend("/like", comment)
            }
        }
    }
}