package com.cyrillrx.rpg.server.character.domain

data class PlayerCharacter(
    val id: String,
    val name: String,
    val attributes: Map<String, Any> = emptyMap(),
)
