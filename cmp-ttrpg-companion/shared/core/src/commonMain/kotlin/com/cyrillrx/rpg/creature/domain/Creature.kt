package com.cyrillrx.rpg.creature.domain

abstract class Creature(
    open val id: String,
    open val size: Size,
    open val alignment: Alignment,
    open val abilities: Abilities,
    open val armorClass: Int,
    open val maxHitPoints: Int,
    open val speeds: Speeds,
    open val languages: List<String>,
) {
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
