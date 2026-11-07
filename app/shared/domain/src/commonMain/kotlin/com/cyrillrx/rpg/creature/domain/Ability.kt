package com.cyrillrx.rpg.creature.domain

enum class Ability {
    STRENGTH,
    DEXTERITY,
    CONSTITUTION,
    INTELLIGENCE,
    WISDOM,
    CHARISMA,
    ;

    fun toAbilityScore(abilities: Abilities): AbilityScore = when (this) {
        STRENGTH -> abilities.strength
        DEXTERITY -> abilities.dexterity
        CONSTITUTION -> abilities.constitution
        INTELLIGENCE -> abilities.intelligence
        WISDOM -> abilities.wisdom
        CHARISMA -> abilities.charisma
    }
}
