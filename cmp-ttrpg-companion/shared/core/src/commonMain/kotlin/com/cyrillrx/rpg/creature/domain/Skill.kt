package com.cyrillrx.rpg.creature.domain

enum class Skill {
    ACROBATICS,
    ANIMAL_HANDLING,
    ARCANA,
    ATHLETICS,
    DECEPTION,
    HISTORY,
    INSIGHT,
    INTIMIDATION,
    INVESTIGATION,
    MEDICINE,
    NATURE,
    PERCEPTION,
    PERFORMANCE,
    PERSUASION,
    RELIGION,
    SLEIGHT_OF_HAND,
    STEALTH,
    SURVIVAL,
    ;

    fun getRelatedAbility(): Ability = when (this) {
        ACROBATICS -> Ability.DEXTERITY
        ANIMAL_HANDLING -> Ability.WISDOM
        ARCANA -> Ability.INTELLIGENCE
        ATHLETICS -> Ability.STRENGTH
        DECEPTION -> Ability.CHARISMA
        HISTORY -> Ability.INTELLIGENCE
        INSIGHT -> Ability.WISDOM
        INTIMIDATION -> Ability.CHARISMA
        INVESTIGATION -> Ability.INTELLIGENCE
        MEDICINE -> Ability.WISDOM
        NATURE -> Ability.INTELLIGENCE
        PERCEPTION -> Ability.WISDOM
        PERFORMANCE -> Ability.CHARISMA
        PERSUASION -> Ability.CHARISMA
        RELIGION -> Ability.INTELLIGENCE
        SLEIGHT_OF_HAND -> Ability.DEXTERITY
        STEALTH -> Ability.DEXTERITY
        SURVIVAL -> Ability.WISDOM
    }

    fun getRelatedAbilityScore(abilities: Abilities): AbilityScore = getRelatedAbility().toAbilityScore(abilities)

    fun computeModifier(abilities: Abilities, proficiency: Proficiency, proficiencyBonus: Int): Int =
        getRelatedAbilityScore(abilities).getModifier() + when (proficiency) {
            Proficiency.NONE -> 0
            Proficiency.PROFICIENT -> proficiencyBonus
            Proficiency.EXPERT -> proficiencyBonus * 2
        }

    fun getProficiency(skills: Skills): Proficiency = when (this) {
        ACROBATICS -> skills.acrobatics
        ANIMAL_HANDLING -> skills.animalHandling
        ARCANA -> skills.arcana
        ATHLETICS -> skills.athletics
        DECEPTION -> skills.deception
        HISTORY -> skills.history
        INSIGHT -> skills.insight
        INTIMIDATION -> skills.intimidation
        INVESTIGATION -> skills.investigation
        MEDICINE -> skills.medicine
        NATURE -> skills.nature
        PERCEPTION -> skills.perception
        PERFORMANCE -> skills.performance
        PERSUASION -> skills.persuasion
        RELIGION -> skills.religion
        SLEIGHT_OF_HAND -> skills.sleightOfHand
        STEALTH -> skills.stealth
        SURVIVAL -> skills.survival
    }
}
