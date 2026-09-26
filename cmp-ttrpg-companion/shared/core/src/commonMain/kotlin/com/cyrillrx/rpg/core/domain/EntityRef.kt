package com.cyrillrx.rpg.core.domain

data class EntityRef(
    val id: String,
    val type: Type,
) {
    enum class Type {
        CREATURE,
        MAGICAL_ITEM,
        SPELL,
    }
}
