package com.cyrillrx.rpg.creature.domain

enum class Proficiency {
    NONE,
    PROFICIENT,
    EXPERT,
    ;

    fun next(): Proficiency = when (this) {
        NONE -> PROFICIENT
        PROFICIENT -> EXPERT
        EXPERT -> NONE
    }

    fun applyMultiplier(proficiencyBonus: Int): Int = when (this) {
        NONE -> 0
        PROFICIENT -> proficiencyBonus
        EXPERT -> proficiencyBonus * 2
    }
}
