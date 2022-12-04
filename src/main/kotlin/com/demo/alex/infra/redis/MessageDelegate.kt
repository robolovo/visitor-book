package com.demo.alex.infra.redis

interface MessageDelegate {
    fun handleMessage(message: String)
}