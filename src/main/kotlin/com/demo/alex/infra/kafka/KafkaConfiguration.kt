package com.demo.alex.infra.kafka

import com.demo.alex.controller.dto.CommentRequest
import com.demo.alex.controller.event.LikeEvent
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.*
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer

@Configuration
@EnableKafka
class KafkaConfiguration {
    private val KAFKA_SERVER_URL = "localhost:9092"
    private val GROUP_ID = "alex"

    @Bean
    fun producerFactory(): ProducerFactory<String, Any> {
        val props: MutableMap<String, Any> = hashMapOf()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = KAFKA_SERVER_URL
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java

        return DefaultKafkaProducerFactory(props)
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, Any> = KafkaTemplate(producerFactory())

    @Bean
    fun consumerFactoryComment(): ConsumerFactory<String, CommentRequest> {
        val props: MutableMap<String, Any> = hashMapOf()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = KAFKA_SERVER_URL
        props[ConsumerConfig.GROUP_ID_CONFIG] = GROUP_ID
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = JsonDeserializer::class.java

        return DefaultKafkaConsumerFactory(props, StringDeserializer(), JsonDeserializer(CommentRequest::class.java))
    }

    @Bean
    fun kafkaListenerContainerFactoryComment(): ConcurrentKafkaListenerContainerFactory<String, CommentRequest> {
        val kafkaListenerContainerFactory = ConcurrentKafkaListenerContainerFactory<String, CommentRequest>()
        kafkaListenerContainerFactory.consumerFactory = consumerFactoryComment()
        return kafkaListenerContainerFactory
    }

    @Bean
    fun consumerFactoryLikeEvent(): ConsumerFactory<String, LikeEvent> {
        val props: MutableMap<String, Any> = hashMapOf()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = KAFKA_SERVER_URL
        props[ConsumerConfig.GROUP_ID_CONFIG] = GROUP_ID
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = JsonDeserializer::class.java

        return DefaultKafkaConsumerFactory(props, StringDeserializer(), JsonDeserializer(LikeEvent::class.java))
    }

    @Bean
    fun kafkaListenerContainerFactoryLikeEvent(): ConcurrentKafkaListenerContainerFactory<String, LikeEvent> {
        val kafkaListenerContainerFactory = ConcurrentKafkaListenerContainerFactory<String, LikeEvent>()
        kafkaListenerContainerFactory.consumerFactory = consumerFactoryLikeEvent()
        return kafkaListenerContainerFactory
    }
}