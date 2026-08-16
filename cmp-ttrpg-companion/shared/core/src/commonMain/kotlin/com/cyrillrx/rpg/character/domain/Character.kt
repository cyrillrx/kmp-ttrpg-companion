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
    val classes: List<ClassLevel> = emptyList(),
) : Creature() {
    /** Sum of every class level; 0 when the character has no class. */
    val totalLevel: Int get() = classes.sumOf { it.level }

    /** Class with the most levels (used for the icon); null when the character has no class. */
    val primaryClass: Class? get() = classes.maxByOrNull { it.level }?.clazz

    fun resolveTranslation(locale: String): Translation? = translations[locale]
        ?: translations[FALLBACK_LOCALE]
        ?: translations.values.firstOrNull()

    fun proficiencyBonus(): Int = when (totalLevel) {
        in 1..4 -> 2
        in 5..8 -> 3
        in 9..12 -> 4
        in 13..16 -> 5
        in 17..20 -> 6
        else -> 0
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
