package com.cyrillrx.rpg.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TtrpgServerApplication

fun main(args: Array<String>) {
    runApplication<TtrpgServerApplication>(*args)
}
