package com.cyrillrx.rpg.character.domain

import com.cyrillrx.core.domain.FALLBACK_LOCALE
import com.cyrillrx.rpg.creature.domain.Abilities
import com.cyrillrx.rpg.creature.domain.Creature
import com.cyrillrx.rpg.creature.domain.Skills
import com.cyrillrx.rpg.creature.domain.Speeds
import kotlinx.serialization.Serializable

@Serializable
data class Character(
    override val id: String,
    val name: String,
    override val size: Size,
    override val alignment: Alignment,
    override val abilities: Abilities,
    override val armorClass: Int,
    override val maxHitPoints: Int,
    override val speeds: Speeds,
    val languages: List<Language>,
    val skills: Skills,
    val race: Race = Race.HUMAN,
    val translations: Map<String, Translation> = emptyMap(),
    val background: Background? = null,
    val currentHitPoints: Int = maxHitPoints,
    val temporaryHitPoints: Int = 0,
    val classes: ClassLevels = DEFAULT_CLASS_LEVELS,
) : Creature() {
    val totalLevel: Int get() = classes.totalLevel

    fun resolveTranslation(locale: String): Translation? = translations[locale]
        ?: translations[FALLBACK_LOCALE]
        ?: translations.values.firstOrNull()

    fun proficiencyBonus(): Int = when (totalLevel.coerceIn(MIN_CHARACTER_LEVEL, MAX_CHARACTER_LEVEL)) {
        in 1..4 -> 2
        in 5..8 -> 3
        in 9..12 -> 4
        in 13..16 -> 5
        else -> 6
    }

    @Serializable
    data class Translation(
        val shortDescription: String = "",
        val description: String = "",
    ) {
        fun isEmpty(): Boolean = shortDescription.isBlank() && description.isBlank()
    }

    enum class Class {
        ARTIFICER,
        BARBARIAN,
        BARD,
        CLERIC,
        DRUID,
        FIGHTER,
        MONK,
        PALADIN,
        RANGER,
        ROGUE,
        SORCERER,
        WARLOCK,
        WIZARD,
        UNKNOWN,
    }
}
