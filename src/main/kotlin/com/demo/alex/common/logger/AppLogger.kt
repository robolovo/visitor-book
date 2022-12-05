package com.demo.alex.common.logger

import mu.KotlinLogging

object AppLogger {

    private val logger = KotlinLogging.logger {}

    fun info(vararg messages: String) {
        for (message in messages) {
            logger.info(message)
        }
    }
}