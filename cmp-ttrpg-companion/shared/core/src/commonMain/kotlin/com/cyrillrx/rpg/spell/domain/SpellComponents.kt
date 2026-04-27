package com.cyrillrx.rpg.spell.domain

import kotlinx.serialization.Serializable

@Serializable
data class SpellComponents(
    val verbal: Boolean,
    val somatic: Boolean,
    val material: Boolean,
) {
    fun toDisplayString(): String = buildList {
        if (verbal) add("V")
        if (somatic) add("S")
        if (material) add("M")
    }.joinToString(", ")
}
