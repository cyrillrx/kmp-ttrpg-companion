package com.cyrillrx.rpg.character.domain

import com.cyrillrx.rpg.creature.domain.Ability
import com.cyrillrx.rpg.creature.domain.Skill

sealed interface D20Check {
    fun modifier(character: Character): Int

    data class SavingThrow(val ability: Ability) : D20Check {
        override fun modifier(character: Character): Int =
            ability.toAbilityScore(character.abilities).getSavingThrow(character.proficiencyBonus())
    }

    data class SkillCheck(val skill: Skill) : D20Check {
        override fun modifier(character: Character): Int = skill.computeModifier(
            abilities = character.abilities,
            proficiency = skill.getProficiency(character.skills),
            proficiencyBonus = character.proficiencyBonus(),
        )
    }

    data object Initiative : D20Check {
        override fun modifier(character: Character): Int = character.initiativeModifier()
    }
}
