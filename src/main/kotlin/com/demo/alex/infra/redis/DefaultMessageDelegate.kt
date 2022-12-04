package com.demo.alex.infra.redis

import com.demo.alex.domain.Comment
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

@Component
class DefaultMessageDelegate(
    private val messagingTemplate: SimpMessagingTemplate,
    private val mapper: ObjectMapper
) : MessageDelegate {

    override fun handleMessage(message: String) {
        println("redis subscribe")
        println(message)
        messagingTemplate.convertAndSend(
            "/queue",
            mapper.readValue(message, Comment::class.java)
        )
    }
}