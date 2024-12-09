package com.cyrillrx.rpg.spellbook.domain

import com.cyrillrx.rpg.core.domain.Character
import kotlinx.serialization.Serializable

@Serializable
class Spell(
    val title: String,
    val description: String,
    val level: Int,
    val castingTime: String,
    val range: String,
    val components: String,
    val duration: String,
    val schools: List<School>,
    val availableClasses: List<Character.Class>,
) {
    enum class School {
        ABJURATION,
        CONJURATION,
        DIVINATION,
        ENCHANTMENT,
        EVOCATION,
        ILLUSION,
        NECROMANCY,
        TRANSMUTATION,
    }
}
