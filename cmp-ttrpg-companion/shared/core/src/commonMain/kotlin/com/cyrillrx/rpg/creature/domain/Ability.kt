package com.cyrillrx.rpg.creature.domain

enum class Ability {
    STRENGTH,
    DEXTERITY,
    CONSTITUTION,
    INTELLIGENCE,
    WISDOM,
    CHARISMA,
}

fun Ability.toAbilityScore(abilities: Abilities): AbilityScore = when (this) {
    Ability.STRENGTH -> abilities.strength
    Ability.DEXTERITY -> abilities.dexterity
    Ability.CONSTITUTION -> abilities.constitution
    Ability.INTELLIGENCE -> abilities.intelligence
    Ability.WISDOM -> abilities.wisdom
    Ability.CHARISMA -> abilities.charisma
}
