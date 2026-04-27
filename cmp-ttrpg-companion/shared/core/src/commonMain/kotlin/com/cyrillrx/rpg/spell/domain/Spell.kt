package com.cyrillrx.rpg.spell.domain

import com.cyrillrx.rpg.character.domain.PlayerCharacter
import kotlinx.serialization.Serializable

@Serializable
class Spell(
    val id: String,
    val source: String,
    val title: String,
    val description: String,
    val level: Int,
    val school: School,
    val concentration: Boolean,
    val ritual: Boolean,
    val castingTime: String,
    val range: String,
    val duration: String,
    val components: SpellComponents,
    val materialDescription: String?,
    val availableClasses: List<PlayerCharacter.Class>,
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
