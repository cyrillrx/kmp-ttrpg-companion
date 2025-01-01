package com.cyrillrx.rpg.character.domain

import com.cyrillrx.rpg.creature.domain.Abilities
import com.cyrillrx.rpg.creature.domain.Creature

class PlayerCharacter(
    val id: String,
    name: String,
    description: String,
    type: Type,
    subtype: String,
    size: Size,
    alignment: Alignment,
    abilities: Abilities,
    armorClass: Int,
    maxHitPoints: Int,
    speed: String,
    languages: List<String>,
    val level: Int,
    val clazz: Class,
    val initiativeModifier: Int = abilities.dex.modifier,
    val skills: Skills,
) : Creature(name, description, type, subtype, size, alignment, abilities, armorClass, maxHitPoints, speed, languages) {

    val proficiencyBonus: Int = when (level) {
        in 1..4 -> 2
        in 5..8 -> 3
        in 9..12 -> 4
        in 13..16 -> 5
        in 17..20 -> 6
        else -> 0
    }

    enum class Class {
        BARD,
        CLERIC,
        DRUID,
        PALADIN,
        RANGER,
        SORCERER,
        WARLOCK,
        WIZARD,
    }
}
