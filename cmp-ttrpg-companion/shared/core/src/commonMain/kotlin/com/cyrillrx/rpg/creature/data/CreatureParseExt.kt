package com.cyrillrx.rpg.creature.data

import com.cyrillrx.rpg.creature.data.api.ApiConditionImmunities
import com.cyrillrx.rpg.creature.data.api.ApiDamageAffinities
import com.cyrillrx.rpg.creature.data.api.ApiSkills
import com.cyrillrx.rpg.creature.domain.ConditionImmunities
import com.cyrillrx.rpg.creature.domain.Creature
import com.cyrillrx.rpg.creature.domain.DamageAffinities
import com.cyrillrx.rpg.creature.domain.DamageAffinity
import com.cyrillrx.rpg.creature.domain.Proficiency
import com.cyrillrx.rpg.creature.domain.Skills

internal fun String.toSize(): Creature.Size? =
    Creature.Size.entries.find { it.name.equals(this, ignoreCase = true) }

internal fun String.toAlignment(): Creature.Alignment? =
    Creature.Alignment.entries.find { it.name.equals(this, ignoreCase = true) }

internal fun String?.toProficiency(): Proficiency = when (this) {
    "proficient" -> Proficiency.PROFICIENT
    "expert" -> Proficiency.EXPERT
    else -> Proficiency.NONE
}

internal fun ApiSkills.toSkills(): Skills = Skills(
    acrobatics = acrobatics.toProficiency(),
    animalHandling = animalHandling.toProficiency(),
    arcana = arcana.toProficiency(),
    athletics = athletics.toProficiency(),
    deception = deception.toProficiency(),
    history = history.toProficiency(),
    insight = insight.toProficiency(),
    intimidation = intimidation.toProficiency(),
    investigation = investigation.toProficiency(),
    medicine = medicine.toProficiency(),
    nature = nature.toProficiency(),
    perception = perception.toProficiency(),
    performance = performance.toProficiency(),
    persuasion = persuasion.toProficiency(),
    religion = religion.toProficiency(),
    sleightOfHand = sleightOfHand.toProficiency(),
    stealth = stealth.toProficiency(),
    survival = survival.toProficiency(),
)

internal fun String?.toDamageAffinity(): DamageAffinity = when (this) {
    "resistant" -> DamageAffinity.RESISTANT
    "immune" -> DamageAffinity.IMMUNE
    "vulnerable" -> DamageAffinity.VULNERABLE
    else -> DamageAffinity.NONE
}

internal fun ApiDamageAffinities.toDamageAffinities(): DamageAffinities = DamageAffinities(
    acid = acid.toDamageAffinity(),
    bludgeoning = bludgeoning.toDamageAffinity(),
    cold = cold.toDamageAffinity(),
    fire = fire.toDamageAffinity(),
    force = force.toDamageAffinity(),
    lightning = lightning.toDamageAffinity(),
    necrotic = necrotic.toDamageAffinity(),
    piercing = piercing.toDamageAffinity(),
    poison = poison.toDamageAffinity(),
    psychic = psychic.toDamageAffinity(),
    radiant = radiant.toDamageAffinity(),
    slashing = slashing.toDamageAffinity(),
    thunder = thunder.toDamageAffinity(),
    bludgeoningNonMagical = bludgeoningNonMagical.toDamageAffinity(),
    piercingNonMagical = piercingNonMagical.toDamageAffinity(),
    slashingNonMagical = slashingNonMagical.toDamageAffinity(),
)

internal fun ApiConditionImmunities.toConditionImmunities(): ConditionImmunities = ConditionImmunities(
    blinded = blinded ?: false,
    charmed = charmed ?: false,
    deafened = deafened ?: false,
    exhaustion = exhaustion ?: false,
    frightened = frightened ?: false,
    grappled = grappled ?: false,
    incapacitated = incapacitated ?: false,
    paralyzed = paralyzed ?: false,
    petrified = petrified ?: false,
    poisoned = poisoned ?: false,
    prone = prone ?: false,
    restrained = restrained ?: false,
    stunned = stunned ?: false,
    unconscious = unconscious ?: false,
)
