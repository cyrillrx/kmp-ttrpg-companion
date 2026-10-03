package com.cyrillrx.rpg.character.data

import com.cyrillrx.rpg.character.data.api.ApiCharacter
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.creature.data.api.ApiAbilities
import com.cyrillrx.rpg.creature.data.api.ApiSavingThrows
import com.cyrillrx.rpg.creature.data.api.ApiSkills
import com.cyrillrx.rpg.creature.data.api.ApiSpeeds
import com.cyrillrx.rpg.creature.domain.Abilities
import com.cyrillrx.rpg.creature.domain.Proficiency
import com.cyrillrx.rpg.creature.domain.Skills
import com.cyrillrx.rpg.creature.domain.Speeds

internal fun Character.toApiCharacter(): ApiCharacter = ApiCharacter(
    id = id,
    name = name,
    background = background?.name?.lowercase(),
    race = race.name.lowercase(),
    classes = classesPrimaryFirst().mapKeys { (clazz, _) -> clazz.name.lowercase() },
    size = size.name.lowercase(),
    alignment = alignment.name.lowercase(),
    abilities = abilities.toApiAbilities(),
    savingThrows = abilities.toApiSavingThrows(),
    armorClass = armorClass,
    maxHitPoints = maxHitPoints,
    currentHitPoints = currentHitPoints,
    temporaryHitPoints = temporaryHitPoints,
    speeds = speeds.toApiSpeeds(),
    skills = skills.toApiSkills(),
    languages = languages.map { it.name.lowercase() },
    translations = translations.mapValues { (_, translation) ->
        ApiCharacter.Translation(
            shortDescription = translation.shortDescription,
            description = translation.description,
        )
    },
)

private fun Character.classesPrimaryFirst(): Map<Character.Class, Int> {
    val primaryLevel = classes[primaryClass] ?: return classes
    return mapOf(primaryClass to primaryLevel) + (classes - primaryClass)
}

private fun Abilities.toApiAbilities(): ApiAbilities = ApiAbilities(
    str = strength.value,
    dex = dexterity.value,
    con = constitution.value,
    int = intelligence.value,
    wis = wisdom.value,
    cha = charisma.value,
)

private fun Abilities.toApiSavingThrows(): ApiSavingThrows = ApiSavingThrows(
    str = strength.savingThrowProficiency.toApiProficiency(),
    dex = dexterity.savingThrowProficiency.toApiProficiency(),
    con = constitution.savingThrowProficiency.toApiProficiency(),
    int = intelligence.savingThrowProficiency.toApiProficiency(),
    wis = wisdom.savingThrowProficiency.toApiProficiency(),
    cha = charisma.savingThrowProficiency.toApiProficiency(),
)

private fun Speeds.toApiSpeeds(): ApiSpeeds = ApiSpeeds(
    walk = walk,
    fly = fly,
    swim = swim,
    climb = climb,
    burrow = burrow,
    hover = hover,
)

private fun Skills.toApiSkills(): ApiSkills = ApiSkills(
    acrobatics = acrobatics.toApiProficiency(),
    animalHandling = animalHandling.toApiProficiency(),
    arcana = arcana.toApiProficiency(),
    athletics = athletics.toApiProficiency(),
    deception = deception.toApiProficiency(),
    history = history.toApiProficiency(),
    insight = insight.toApiProficiency(),
    intimidation = intimidation.toApiProficiency(),
    investigation = investigation.toApiProficiency(),
    medicine = medicine.toApiProficiency(),
    nature = nature.toApiProficiency(),
    perception = perception.toApiProficiency(),
    performance = performance.toApiProficiency(),
    persuasion = persuasion.toApiProficiency(),
    religion = religion.toApiProficiency(),
    sleightOfHand = sleightOfHand.toApiProficiency(),
    stealth = stealth.toApiProficiency(),
    survival = survival.toApiProficiency(),
)

private fun Proficiency.toApiProficiency(): String? = when (this) {
    Proficiency.NONE -> null
    Proficiency.PROFICIENT -> "proficient"
    Proficiency.EXPERT -> "expert"
}
