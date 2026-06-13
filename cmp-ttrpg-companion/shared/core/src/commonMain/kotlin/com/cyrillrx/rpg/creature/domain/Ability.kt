package com.cyrillrx.rpg.creature.domain

import com.cyrillrx.rpg.core.domain.toSignedString
import kotlinx.serialization.Serializable

@Serializable
data class Ability(
    val value: Int,
    val savingThrowProficiency: Proficiency = Proficiency.NONE,
) {
    fun getModifier(): Int = computeModifier(value)

    fun getValueWithModifier(): String = "$value (${getModifier().toSignedString()})"

    fun getSavingThrow(proficiencyBonus: Int): Int = getModifier() + when (savingThrowProficiency) {
        Proficiency.NONE -> 0
        Proficiency.PROFICIENT -> proficiencyBonus
        Proficiency.EXPERT -> proficiencyBonus * 2
    }

    companion object {
        const val DEFAULT_VALUE = 10

        fun computeModifier(abilityValue: Int): Int = (abilityValue - 10).floorDiv(2)
    }
}
