package com.cyrillrx.rpg.spell.domain

import com.cyrillrx.rpg.character.domain.PlayerCharacter
import kotlinx.serialization.Serializable

private const val FALLBACK_LOCALE = "en"

@Serializable
class Spell(
    val id: String,
    val source: String,
    val level: Int,
    val school: School,
    val concentration: Boolean,
    val ritual: Boolean,
    val components: Components,
    val availableClasses: List<PlayerCharacter.Class>,
    val translations: Map<String, Translation>,
) {
    init {
        require(translations.isNotEmpty()) { "Spell $id must have at least one translation" }
    }

    fun resolveTranslation(locale: String): Translation =
        translations[locale]
            ?: translations[FALLBACK_LOCALE]
            ?: requireNotNull(translations.entries.minByOrNull { it.key }?.value) {
                "Spell $id has empty translations"
            }

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
    data class Components(
        val verbal: Boolean,
        val somatic: Boolean,
        val material: Boolean,
    )

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
