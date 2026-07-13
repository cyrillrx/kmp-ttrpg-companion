package com.cyrillrx.rpg.core.presentation.component.dnd

import androidx.compose.runtime.Composable
import com.cyrillrx.rpg.core.domain.toSignedString
import com.cyrillrx.rpg.creature.domain.AbilityScore
import com.cyrillrx.rpg.creature.domain.DamageAffinity
import com.cyrillrx.rpg.creature.domain.Monster
import com.cyrillrx.rpg.creature.domain.Proficiency
import com.cyrillrx.rpg.creature.domain.Skill
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.ability_label_cha
import rpg_companion.composeapp.generated.resources.ability_label_con
import rpg_companion.composeapp.generated.resources.ability_label_dex
import rpg_companion.composeapp.generated.resources.ability_label_int
import rpg_companion.composeapp.generated.resources.ability_label_str
import rpg_companion.composeapp.generated.resources.ability_label_wis
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
fun Monster.getFormattedSavingThrows(): String {
    val proficiencyBonus = proficiencyBonus()
    return buildList {
        fun appendSave(label: String, score: AbilityScore) {
            if (score.savingThrowProficiency != Proficiency.NONE) {
                add("$label ${score.getSavingThrow(proficiencyBonus).toSignedString()}")
            }
        }
        appendSave(stringResource(Res.string.ability_label_str), abilities.strength)
        appendSave(stringResource(Res.string.ability_label_dex), abilities.dexterity)
        appendSave(stringResource(Res.string.ability_label_con), abilities.constitution)
        appendSave(stringResource(Res.string.ability_label_int), abilities.intelligence)
        appendSave(stringResource(Res.string.ability_label_wis), abilities.wisdom)
        appendSave(stringResource(Res.string.ability_label_cha), abilities.charisma)
    }.joinToString(", ")
}

@Composable
fun Monster.getFormattedSkills(): String {
    val proficiencyBonus = proficiencyBonus()
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
fun Monster.getFormattedDamageAffinities(target: DamageAffinity): String = buildList {
    val affinities = damageAffinities
    if (affinities.acid == target) add(stringResource(Res.string.damage_type_acid))
    if (affinities.bludgeoning == target) add(stringResource(Res.string.damage_type_bludgeoning))
    if (affinities.cold == target) add(stringResource(Res.string.damage_type_cold))
    if (affinities.fire == target) add(stringResource(Res.string.damage_type_fire))
    if (affinities.force == target) add(stringResource(Res.string.damage_type_force))
    if (affinities.lightning == target) add(stringResource(Res.string.damage_type_lightning))
    if (affinities.necrotic == target) add(stringResource(Res.string.damage_type_necrotic))
    if (affinities.piercing == target) add(stringResource(Res.string.damage_type_piercing))
    if (affinities.poison == target) add(stringResource(Res.string.damage_type_poison))
    if (affinities.psychic == target) add(stringResource(Res.string.damage_type_psychic))
    if (affinities.radiant == target) add(stringResource(Res.string.damage_type_radiant))
    if (affinities.slashing == target) add(stringResource(Res.string.damage_type_slashing))
    if (affinities.thunder == target) add(stringResource(Res.string.damage_type_thunder))
    if (affinities.bludgeoningNonMagical == target) add(stringResource(Res.string.damage_type_bludgeoning_nonmagical))
    if (affinities.piercingNonMagical == target) add(stringResource(Res.string.damage_type_piercing_nonmagical))
    if (affinities.slashingNonMagical == target) add(stringResource(Res.string.damage_type_slashing_nonmagical))
}.joinToString(", ")

@Composable
fun Monster.getFormattedConditionImmunities(): String = buildList {
    val immunities = conditionImmunities
    if (immunities.blinded) add(stringResource(Res.string.condition_blinded))
    if (immunities.charmed) add(stringResource(Res.string.condition_charmed))
    if (immunities.deafened) add(stringResource(Res.string.condition_deafened))
    if (immunities.exhaustion) add(stringResource(Res.string.condition_exhaustion))
    if (immunities.frightened) add(stringResource(Res.string.condition_frightened))
    if (immunities.grappled) add(stringResource(Res.string.condition_grappled))
    if (immunities.incapacitated) add(stringResource(Res.string.condition_incapacitated))
    if (immunities.paralyzed) add(stringResource(Res.string.condition_paralyzed))
    if (immunities.petrified) add(stringResource(Res.string.condition_petrified))
    if (immunities.poisoned) add(stringResource(Res.string.condition_poisoned))
    if (immunities.prone) add(stringResource(Res.string.condition_prone))
    if (immunities.restrained) add(stringResource(Res.string.condition_restrained))
    if (immunities.stunned) add(stringResource(Res.string.condition_stunned))
    if (immunities.unconscious) add(stringResource(Res.string.condition_unconscious))
}.joinToString(", ")
