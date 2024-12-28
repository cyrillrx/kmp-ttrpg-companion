package com.cyrillrx.rpg.creature.domain

open class Creature(
    val name: String,
    val description: String,
    val type: Type,
    val subtype: String,
    val size: Size,
    val alignment: Alignment,
    val abilities: Abilities,
    val armorClass: Int,
    val maxHitPoints: Int,
    val speed: String,
    val languages: List<String>,
) {
    enum class Type {
        ABERRATION,
        BEAST,
        CELESTIAL,
        CONSTRUCT,
        DRAGON,
        ELEMENTAL,
        FEY,
        FIEND,
        GIANT,
        HUMANOID,
        MONSTROSITY,
        OOZE,
        PLANT,
        UNDEAD,
        UNKNOWN,
    }

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
