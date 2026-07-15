package com.cyrillrx.rpg.core.presentation.component.dnd

import androidx.compose.runtime.Composable
import com.cyrillrx.rpg.core.domain.toSignedString
import com.cyrillrx.rpg.creature.domain.Ability
import com.cyrillrx.rpg.creature.domain.Condition
import com.cyrillrx.rpg.creature.domain.DamageAffinity
import com.cyrillrx.rpg.creature.domain.DamageType
import com.cyrillrx.rpg.creature.domain.Monster
import com.cyrillrx.rpg.creature.domain.Proficiency
import com.cyrillrx.rpg.creature.domain.Skill
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.condition_blinded
import rpg_companion.composeapp.generated.resources.condition_charmed
import rpg_companion.composeapp.generated.resources.condition_deafened
import rpg_companion.composeapp.generated.resources.condition_exhaustion
import rpg_companion.composeapp.generated.resources.condition_frightened
import rpg_companion.composeapp.generated.resources.condition_grappled
import rpg_companion.composeapp.generated.resources.condition_incapacitated
import rpg_companion.composeapp.generated.resources.condition_paralyzed
import rpg_companion.composeapp.generated.resources.condition_petrified
import rpg_companion.composeapp.generated.resources.condition_poisoned
import rpg_companion.composeapp.generated.resources.condition_prone
import rpg_companion.composeapp.generated.resources.condition_restrained
import rpg_companion.composeapp.generated.resources.condition_stunned
import rpg_companion.composeapp.generated.resources.condition_unconscious
import rpg_companion.composeapp.generated.resources.damage_type_acid
import rpg_companion.composeapp.generated.resources.damage_type_bludgeoning
import rpg_companion.composeapp.generated.resources.damage_type_bludgeoning_nonmagical
import rpg_companion.composeapp.generated.resources.damage_type_cold
import rpg_companion.composeapp.generated.resources.damage_type_fire
import rpg_companion.composeapp.generated.resources.damage_type_force
import rpg_companion.composeapp.generated.resources.damage_type_lightning
import rpg_companion.composeapp.generated.resources.damage_type_necrotic
import rpg_companion.composeapp.generated.resources.damage_type_piercing
import rpg_companion.composeapp.generated.resources.damage_type_piercing_nonmagical
import rpg_companion.composeapp.generated.resources.damage_type_poison
import rpg_companion.composeapp.generated.resources.damage_type_psychic
import rpg_companion.composeapp.generated.resources.damage_type_radiant
import rpg_companion.composeapp.generated.resources.damage_type_slashing
import rpg_companion.composeapp.generated.resources.damage_type_slashing_nonmagical
import rpg_companion.composeapp.generated.resources.damage_type_thunder

@Composable
fun Monster.getFormattedSavingThrows(proficiencyBonus: Int): String {
    return buildList {
        Ability.entries.forEach { ability ->
            val score = ability.toAbilityScore(abilities)
            if (score.savingThrowProficiency != Proficiency.NONE) {
                add("${ability.toAbbreviationString()} ${score.getSavingThrow(proficiencyBonus).toSignedString()}")
            }
        }
    }.joinToString(", ")
}

@Composable
fun Monster.getFormattedSkills(proficiencyBonus: Int): String {
    return buildList {
        Skill.entries.forEach { skill ->
            val proficiency = skill.getProficiency(skills)
            if (proficiency != Proficiency.NONE) {
                val modifier = skill.computeModifier(abilities, proficiency, proficiencyBonus)
                add("${skill.toFormattedString()} ${modifier.toSignedString()}")
            }
        }
    }.joinToString(", ")
}

@Composable
fun DamageType.toFormattedString(): String {
    val stringRes = when (this) {
        DamageType.ACID -> Res.string.damage_type_acid
        DamageType.BLUDGEONING -> Res.string.damage_type_bludgeoning
        DamageType.COLD -> Res.string.damage_type_cold
        DamageType.FIRE -> Res.string.damage_type_fire
        DamageType.FORCE -> Res.string.damage_type_force
        DamageType.LIGHTNING -> Res.string.damage_type_lightning
        DamageType.NECROTIC -> Res.string.damage_type_necrotic
        DamageType.PIERCING -> Res.string.damage_type_piercing
        DamageType.POISON -> Res.string.damage_type_poison
        DamageType.PSYCHIC -> Res.string.damage_type_psychic
        DamageType.RADIANT -> Res.string.damage_type_radiant
        DamageType.SLASHING -> Res.string.damage_type_slashing
        DamageType.THUNDER -> Res.string.damage_type_thunder
        DamageType.BLUDGEONING_NONMAGICAL -> Res.string.damage_type_bludgeoning_nonmagical
        DamageType.PIERCING_NONMAGICAL -> Res.string.damage_type_piercing_nonmagical
        DamageType.SLASHING_NONMAGICAL -> Res.string.damage_type_slashing_nonmagical
    }
    return stringResource(stringRes)
}

@Composable
fun Condition.toFormattedString(): String {
    val stringRes = when (this) {
        Condition.BLINDED -> Res.string.condition_blinded
        Condition.CHARMED -> Res.string.condition_charmed
        Condition.DEAFENED -> Res.string.condition_deafened
        Condition.EXHAUSTION -> Res.string.condition_exhaustion
        Condition.FRIGHTENED -> Res.string.condition_frightened
        Condition.GRAPPLED -> Res.string.condition_grappled
        Condition.INCAPACITATED -> Res.string.condition_incapacitated
        Condition.PARALYZED -> Res.string.condition_paralyzed
        Condition.PETRIFIED -> Res.string.condition_petrified
        Condition.POISONED -> Res.string.condition_poisoned
        Condition.PRONE -> Res.string.condition_prone
        Condition.RESTRAINED -> Res.string.condition_restrained
        Condition.STUNNED -> Res.string.condition_stunned
        Condition.UNCONSCIOUS -> Res.string.condition_unconscious
    }
    return stringResource(stringRes)
}

@Composable
fun Monster.getFormattedDamageAffinities(target: DamageAffinity): String = buildList {
    DamageType.entries.forEach { damageType ->
        if (damageType.select(damageAffinities) == target) add(damageType.toFormattedString())
    }
}.joinToString(", ")

@Composable
fun Monster.getFormattedConditionImmunities(): String = buildList {
    Condition.entries.forEach { condition ->
        if (condition.isImmune(conditionImmunities)) add(condition.toFormattedString())
    }
}.joinToString(", ")
