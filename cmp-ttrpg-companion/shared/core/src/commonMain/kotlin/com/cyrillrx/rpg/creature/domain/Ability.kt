package com.cyrillrx.rpg.creature.domain

import kotlinx.serialization.Serializable

@Serializable
class Ability(
    val value: Int,
    val savingThrowProficiency: Proficiency = Proficiency.NONE,
) {
    fun getModifier(): Int = computeModifier(value)

    fun getValueWithModifier(): String = "$value (${getSignedModifier(value)})"

    companion object {
        const val DEFAULT_VALUE = 10

        private fun getSignedModifier(abilityValue: Int): String {
            val modifier = computeModifier(abilityValue)
            return if (modifier >= 0) "+$modifier" else "$modifier"
        }

        private fun computeModifier(abilityValue: Int): Int = when {
            abilityValue < 4 -> -4
            abilityValue < 6 -> -3
            abilityValue < 8 -> -2
            abilityValue < 10 -> -1
            abilityValue < 12 -> 0
            abilityValue < 14 -> 1
            abilityValue < 16 -> 2
            abilityValue < 18 -> 3
            abilityValue < 20 -> 4
            abilityValue < 22 -> 5
            abilityValue < 24 -> 6
            abilityValue < 26 -> 7
            abilityValue < 28 -> 8
            abilityValue < 30 -> 9
            else -> 10
        }
    }
}
