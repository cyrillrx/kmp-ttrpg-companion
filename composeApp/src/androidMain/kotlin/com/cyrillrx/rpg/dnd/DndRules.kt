package com.cyrillrx.rpg.dnd

object DndRules {

    fun getModifier(abilityValue: Int): Int = when {
        abilityValue < 4 -> -4
        abilityValue < 6 -> -3
        abilityValue < 8 -> -2
        abilityValue < 10 -> -1
        abilityValue < 12 -> 0
        abilityValue < 14 -> 1
        abilityValue < 16 -> 2
        abilityValue < 18 -> 3
        abilityValue < 20 -> 4
        else -> 5
    }
}
