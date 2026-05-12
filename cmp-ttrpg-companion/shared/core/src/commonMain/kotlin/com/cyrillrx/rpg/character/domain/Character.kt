package com.cyrillrx.rpg.character.domain

import com.cyrillrx.core.domain.FALLBACK_LOCALE
import com.cyrillrx.rpg.creature.domain.Abilities
import com.cyrillrx.rpg.creature.domain.Creature
import com.cyrillrx.rpg.creature.domain.Language
import com.cyrillrx.rpg.creature.domain.Skills
import com.cyrillrx.rpg.creature.domain.Speeds

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
    val level: Int,
    val clazz: Class,
    val skills: Skills,
    val race: Race = Race.HUMAN,
    val translations: Map<String, Translation> = emptyMap(),
    val background: String? = null,
    val currentHitPoints: Int = maxHitPoints,
    val temporaryHitPoints: Int = 0,
) : Creature(
    id = id,
    size = size,
    alignment = alignment,
    abilities = abilities,
    armorClass = armorClass,
    maxHitPoints = maxHitPoints,
    speeds = speeds,
) {
    fun resolveTranslation(locale: String): Translation? = translations[locale]
        ?: translations[FALLBACK_LOCALE]
        ?: translations.values.firstOrNull()

    fun initiativeModifier(): Int = abilities.dex.modifier

    fun proficiencyBonus(): Int = when (level) {
        in 1..4 -> 2
        in 5..8 -> 3
        in 9..12 -> 4
        in 13..16 -> 5
        in 17..20 -> 6
        else -> 0
    }

    data class Translation(
        val shortDescription: String = "",
        val description: String = "",
    )

    enum class Class {
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
