package com.cyrillrx.rpg.character.domain

import com.cyrillrx.rpg.creature.domain.Ability
import com.cyrillrx.rpg.creature.domain.Skill

sealed interface D20Check {
    data class SavingThrow(val ability: Ability) : D20Check

    data class SkillCheck(val skill: Skill) : D20Check

    data object Initiative : D20Check
}
