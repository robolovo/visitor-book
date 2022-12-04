package com.demo.alex.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.*

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfiguration : WebSocketMessageBrokerConfigurer {
    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableSimpleBroker("/queue")
        registry.setApplicationDestinationPrefixes("/app")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/ws").withSockJS()
    }
}
