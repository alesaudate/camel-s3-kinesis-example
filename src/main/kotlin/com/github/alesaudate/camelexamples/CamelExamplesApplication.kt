package com.github.alesaudate.camelexamples

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CamelExamplesApplication

fun main(args: Array<String>) {
	runApplication<CamelExamplesApplication>(*args)
}
