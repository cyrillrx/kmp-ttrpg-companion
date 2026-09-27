package com.cyrillrx.rpg.creature.domain

import com.cyrillrx.rpg.core.domain.Entity

abstract class Creature : Entity {
    abstract override val id: String
    abstract val size: Size
    abstract val alignment: Alignment
    abstract val abilities: Abilities
    abstract val armorClass: Int
    abstract val maxHitPoints: Int
    abstract val speeds: Speeds

    fun initiativeModifier(): Int = abilities.dexterity.getModifier()

    enum class Size {
        TINY,
        SMALL,
        MEDIUM,
        LARGE,
        HUGE,
        GARGANTUAN,
        MEDIUM_OR_SMALL,
        HUGE_OR_GARGANTUAN,
        HUGE_OR_SMALLER,
        UNKNOWN,
    }

    enum class Alignment {
        LAWFUL_GOOD,
        LAWFUL_NEUTRAL,
        LAWFUL_EVIL,
        NEUTRAL_GOOD,
        NEUTRAL,
        NEUTRAL_EVIL,
        CHAOTIC_GOOD,
        CHAOTIC_NEUTRAL,
        CHAOTIC_EVIL,
        UNALIGNED,
        ANY_ALIGNMENT,
        UNKNOWN,
    }
}
