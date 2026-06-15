package com.cyrillrx.rpg.creature.domain

import com.cyrillrx.rpg.core.domain.toSignedString
import kotlinx.serialization.Serializable

@Serializable
data class AbilityScore(
    val value: Int,
    val savingThrowProficiency: Proficiency = Proficiency.NONE,
) {
    fun getModifier(): Int = computeModifier(value)

    fun getValueWithModifier(): String = "$value (${getModifier().toSignedString()})"

    fun getSavingThrow(proficiencyBonus: Int): Int = getModifier() + savingThrowProficiency.applyMultiplier(proficiencyBonus)

    companion object {
        const val DEFAULT_VALUE = 10

        fun computeModifier(abilityValue: Int): Int = (abilityValue - 10).floorDiv(2)
    }
}
