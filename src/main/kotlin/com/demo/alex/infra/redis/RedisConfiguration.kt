package com.demo.alex.infra.redis

import com.demo.alex.controller.dto.CommentRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer


@Configuration
@EnableRedisRepositories
class RedisConfiguration(
    @Value("\${spring.redis.host}")
    private val host: String,
    @Value("\${spring.redis.port}")
    private val port: Int
) {

    @Bean
    fun redisConnectionFactory(): LettuceConnectionFactory =
        LettuceConnectionFactory(RedisStandaloneConfiguration(host, port))

    @Bean
    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory, mapper: ObjectMapper): RedisTemplate<String, String> {
        val redisTemplate = RedisTemplate<String, String>()
        redisTemplate.setConnectionFactory(redisConnectionFactory)
        redisTemplate.setEnableTransactionSupport(true)

        val jackson2JsonRedisSerializer = Jackson2JsonRedisSerializer(CommentRequest::class.java)
        jackson2JsonRedisSerializer.setObjectMapper(mapper)
        redisTemplate.keySerializer = StringRedisSerializer()
        redisTemplate.valueSerializer = jackson2JsonRedisSerializer

        return redisTemplate
    }

    @Bean
    fun redisMessageListenerContainer(
        containerFactory: RedisConnectionFactory,
        listener: DefaultMessageDelegate
    ): RedisMessageListenerContainer {
        val listenerContainer = RedisMessageListenerContainer()
        listenerContainer.setConnectionFactory(containerFactory)

        val messageListenerAdapter = MessageListenerAdapter(listener, "handleMessage")
        listenerContainer.addMessageListener(messageListenerAdapter, ChannelTopic.of("comment"))
        messageListenerAdapter.afterPropertiesSet()

        return listenerContainer
    }
}