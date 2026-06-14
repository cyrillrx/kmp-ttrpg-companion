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
}

fun Skill.getRelatedAbility(abilities: Abilities): AbilityScore = when (this) {
    Skill.ACROBATICS -> abilities.dexterity
    Skill.ANIMAL_HANDLING -> abilities.wisdom
    Skill.ARCANA -> abilities.intelligence
    Skill.ATHLETICS -> abilities.strength
    Skill.DECEPTION -> abilities.charisma
    Skill.HISTORY -> abilities.intelligence
    Skill.INSIGHT -> abilities.wisdom
    Skill.INTIMIDATION -> abilities.charisma
    Skill.INVESTIGATION -> abilities.intelligence
    Skill.MEDICINE -> abilities.wisdom
    Skill.NATURE -> abilities.intelligence
    Skill.PERCEPTION -> abilities.wisdom
    Skill.PERFORMANCE -> abilities.charisma
    Skill.PERSUASION -> abilities.charisma
    Skill.RELIGION -> abilities.intelligence
    Skill.SLEIGHT_OF_HAND -> abilities.dexterity
    Skill.STEALTH -> abilities.dexterity
    Skill.SURVIVAL -> abilities.wisdom
}

fun Skill.getProficiency(skills: Skills): Proficiency = when (this) {
    Skill.ACROBATICS -> skills.acrobatics
    Skill.ANIMAL_HANDLING -> skills.animalHandling
    Skill.ARCANA -> skills.arcana
    Skill.ATHLETICS -> skills.athletics
    Skill.DECEPTION -> skills.deception
    Skill.HISTORY -> skills.history
    Skill.INSIGHT -> skills.insight
    Skill.INTIMIDATION -> skills.intimidation
    Skill.INVESTIGATION -> skills.investigation
    Skill.MEDICINE -> skills.medicine
    Skill.NATURE -> skills.nature
    Skill.PERCEPTION -> skills.perception
    Skill.PERFORMANCE -> skills.performance
    Skill.PERSUASION -> skills.persuasion
    Skill.RELIGION -> skills.religion
    Skill.SLEIGHT_OF_HAND -> skills.sleightOfHand
    Skill.STEALTH -> skills.stealth
    Skill.SURVIVAL -> skills.survival
}

fun Skills.withProficiency(skill: Skill, proficiency: Proficiency): Skills = when (skill) {
    Skill.ACROBATICS -> copy(acrobatics = proficiency)
    Skill.ANIMAL_HANDLING -> copy(animalHandling = proficiency)
    Skill.ARCANA -> copy(arcana = proficiency)
    Skill.ATHLETICS -> copy(athletics = proficiency)
    Skill.DECEPTION -> copy(deception = proficiency)
    Skill.HISTORY -> copy(history = proficiency)
    Skill.INSIGHT -> copy(insight = proficiency)
    Skill.INTIMIDATION -> copy(intimidation = proficiency)
    Skill.INVESTIGATION -> copy(investigation = proficiency)
    Skill.MEDICINE -> copy(medicine = proficiency)
    Skill.NATURE -> copy(nature = proficiency)
    Skill.PERCEPTION -> copy(perception = proficiency)
    Skill.PERFORMANCE -> copy(performance = proficiency)
    Skill.PERSUASION -> copy(persuasion = proficiency)
    Skill.RELIGION -> copy(religion = proficiency)
    Skill.SLEIGHT_OF_HAND -> copy(sleightOfHand = proficiency)
    Skill.STEALTH -> copy(stealth = proficiency)
    Skill.SURVIVAL -> copy(survival = proficiency)
}
