package com.cyrillrx.rpg.creature.domain

abstract class Creature {
    abstract val id: String
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
        UNKNOWN,
    }
}
