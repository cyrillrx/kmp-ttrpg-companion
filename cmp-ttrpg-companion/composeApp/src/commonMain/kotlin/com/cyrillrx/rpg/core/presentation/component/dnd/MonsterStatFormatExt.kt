package com.cyrillrx.rpg.core.presentation.component.dnd

import androidx.compose.runtime.Composable
import com.cyrillrx.rpg.core.domain.toSignedString
import com.cyrillrx.rpg.creature.domain.AbilityScore
import com.cyrillrx.rpg.creature.domain.ConditionImmunities
import com.cyrillrx.rpg.creature.domain.DamageAffinities
import com.cyrillrx.rpg.creature.domain.DamageAffinity
import com.cyrillrx.rpg.creature.domain.Monster
import com.cyrillrx.rpg.creature.domain.Proficiency
import com.cyrillrx.rpg.creature.domain.Skill
import org.jetbrains.compose.resources.StringResource
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
import kotlin.reflect.KProperty1

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

private val damageTypeResources: List<Pair<KProperty1<DamageAffinities, DamageAffinity>, StringResource>> = listOf(
    DamageAffinities::acid to Res.string.damage_type_acid,
    DamageAffinities::bludgeoning to Res.string.damage_type_bludgeoning,
    DamageAffinities::cold to Res.string.damage_type_cold,
    DamageAffinities::fire to Res.string.damage_type_fire,
    DamageAffinities::force to Res.string.damage_type_force,
    DamageAffinities::lightning to Res.string.damage_type_lightning,
    DamageAffinities::necrotic to Res.string.damage_type_necrotic,
    DamageAffinities::piercing to Res.string.damage_type_piercing,
    DamageAffinities::poison to Res.string.damage_type_poison,
    DamageAffinities::psychic to Res.string.damage_type_psychic,
    DamageAffinities::radiant to Res.string.damage_type_radiant,
    DamageAffinities::slashing to Res.string.damage_type_slashing,
    DamageAffinities::thunder to Res.string.damage_type_thunder,
    DamageAffinities::bludgeoningNonMagical to Res.string.damage_type_bludgeoning_nonmagical,
    DamageAffinities::piercingNonMagical to Res.string.damage_type_piercing_nonmagical,
    DamageAffinities::slashingNonMagical to Res.string.damage_type_slashing_nonmagical,
)

private val conditionImmunityResources: List<Pair<KProperty1<ConditionImmunities, Boolean>, StringResource>> = listOf(
    ConditionImmunities::blinded to Res.string.condition_blinded,
    ConditionImmunities::charmed to Res.string.condition_charmed,
    ConditionImmunities::deafened to Res.string.condition_deafened,
    ConditionImmunities::exhaustion to Res.string.condition_exhaustion,
    ConditionImmunities::frightened to Res.string.condition_frightened,
    ConditionImmunities::grappled to Res.string.condition_grappled,
    ConditionImmunities::incapacitated to Res.string.condition_incapacitated,
    ConditionImmunities::paralyzed to Res.string.condition_paralyzed,
    ConditionImmunities::petrified to Res.string.condition_petrified,
    ConditionImmunities::poisoned to Res.string.condition_poisoned,
    ConditionImmunities::prone to Res.string.condition_prone,
    ConditionImmunities::restrained to Res.string.condition_restrained,
    ConditionImmunities::stunned to Res.string.condition_stunned,
    ConditionImmunities::unconscious to Res.string.condition_unconscious,
)

@Composable
fun Monster.getFormattedDamageAffinities(target: DamageAffinity): String = buildList {
    damageTypeResources.forEach { (affinity, resource) ->
        if (affinity.get(damageAffinities) == target) add(stringResource(resource))
    }
}.joinToString(", ")

@Composable
fun Monster.getFormattedConditionImmunities(): String = buildList {
    conditionImmunityResources.forEach { (immunity, resource) ->
        if (immunity.get(conditionImmunities)) add(stringResource(resource))
    }
}.joinToString(", ")
