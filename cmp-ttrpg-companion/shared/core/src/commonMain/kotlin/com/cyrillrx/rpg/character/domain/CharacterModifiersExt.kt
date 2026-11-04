package com.cyrillrx.rpg.character.domain

import com.cyrillrx.rpg.creature.domain.Ability
import com.cyrillrx.rpg.creature.domain.Skill

fun Character.savingThrowModifier(ability: Ability): Int =
    ability.toAbilityScore(abilities).getSavingThrow(proficiencyBonus())

fun Character.skillModifier(skill: Skill): Int =
    skill.computeModifier(abilities, skill.getProficiency(skills), proficiencyBonus())

fun Character.modifierFor(check: D20Check): Int = when (check) {
    is D20Check.SavingThrow -> savingThrowModifier(check.ability)
    is D20Check.SkillCheck -> skillModifier(check.skill)
    D20Check.Initiative -> initiativeModifier()
}
