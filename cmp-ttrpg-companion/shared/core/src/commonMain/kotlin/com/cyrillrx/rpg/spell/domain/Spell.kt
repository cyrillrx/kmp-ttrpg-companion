package com.cyrillrx.rpg.spell.domain

import com.cyrillrx.rpg.character.domain.PlayerCharacter
import kotlinx.serialization.Serializable

@Serializable
class Spell(
    val id: String,
    val source: String,
    val level: Int,
    val school: School,
    val concentration: Boolean,
    val ritual: Boolean,
    val components: SpellComponents,
    val availableClasses: List<PlayerCharacter.Class>,
    val translations: Map<String, Translation>,
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

    @Serializable
    data class Translation(
        val name: String,
        val castingTime: String,
        val range: String,
        val duration: String,
        val materialDescription: String?,
        val description: String,
    )
}

private const val FALLBACK_LOCALE = "en"

fun Map<String, Spell.Translation>.resolve(locale: String): Spell.Translation? {
    if (isEmpty()) return null
    return get(locale) ?: get(FALLBACK_LOCALE) ?: entries.minByOrNull { it.key }?.value
}
