package com.cyrillrx.rpg.core

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform