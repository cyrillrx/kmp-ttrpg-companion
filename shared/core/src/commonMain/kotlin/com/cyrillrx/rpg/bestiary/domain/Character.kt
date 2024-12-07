package com.cyrillrx.rpg.bestiary.domain

open class Character(
    val name: String,
    val description: String,
    val type: String,
    val subtype: String,
    val size: String,
    val alignment: String,
    val abilities: Abilities,
) {
    val ac: String? = null
    val hp: String? = null
    val speed: String? = null
    val languages: String? = null

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
    }

    enum class Size(label: String, shortLabel: String, space: Float, samples: String) {
        TINY("Très Petite", "TP", 0.75f, "Diablotin, esprit follet"),
        SMALL("Petite", "P", 1.5f, "Rat géant, gobelin"),
        MEDIUM("Moyenne", "M", 1.5f, "Orque, loup-garou"),
        LARGE("Grande", "G", 3f, "Hippogriffe, ogre"),
        HUGE("Très grande", "TG", 4.5f, "Géant de feu, sylvanien"),
        GARGANTUAN("Gigantesque", "Gig", 6f, "Kraken, ver pourpre"),
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
    }
}
