package com.cyrillrx.rpg.creature.domain

abstract class BaseCreature(
    val id: String,
    val name: String,
    val description: String,
    val size: Size,
    val alignment: Alignment,
    val abilities: Abilities,
    val armorClass: Int,
    val maxHitPoints: Int,
    val speed: String,
    val languages: List<String>,
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
