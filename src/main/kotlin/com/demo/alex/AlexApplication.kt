package com.demo.alex

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AlexApplication

fun main(args: Array<String>) {
	runApplication<AlexApplication>(*args)
}
