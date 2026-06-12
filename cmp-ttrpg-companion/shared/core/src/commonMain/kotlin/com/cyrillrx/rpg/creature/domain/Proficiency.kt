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
}
