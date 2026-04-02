package com.cyrillrx.rpg.core.domain

data class Entity(
    val id: String,
    val type: Type,
) {
    enum class Type {
        CREATURE,
        MAGICAL_ITEM,
        SPELL,
    }
}
