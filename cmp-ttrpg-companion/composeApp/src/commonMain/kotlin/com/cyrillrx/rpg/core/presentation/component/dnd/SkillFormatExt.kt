package com.cyrillrx.rpg.core.presentation.component.dnd

import androidx.compose.runtime.Composable
import com.cyrillrx.rpg.creature.domain.Skill
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.skill_acrobatics
import rpg_companion.composeapp.generated.resources.skill_animal_handling
import rpg_companion.composeapp.generated.resources.skill_arcana
import rpg_companion.composeapp.generated.resources.skill_athletics
import rpg_companion.composeapp.generated.resources.skill_deception
import rpg_companion.composeapp.generated.resources.skill_history
import rpg_companion.composeapp.generated.resources.skill_insight
import rpg_companion.composeapp.generated.resources.skill_intimidation
import rpg_companion.composeapp.generated.resources.skill_investigation
import rpg_companion.composeapp.generated.resources.skill_medicine
import rpg_companion.composeapp.generated.resources.skill_nature
import rpg_companion.composeapp.generated.resources.skill_perception
import rpg_companion.composeapp.generated.resources.skill_performance
import rpg_companion.composeapp.generated.resources.skill_persuasion
import rpg_companion.composeapp.generated.resources.skill_religion
import rpg_companion.composeapp.generated.resources.skill_sleight_of_hand
import rpg_companion.composeapp.generated.resources.skill_stealth
import rpg_companion.composeapp.generated.resources.skill_survival

fun Skill.toStringRes(): StringResource = when (this) {
    Skill.ACROBATICS -> Res.string.skill_acrobatics
    Skill.ANIMAL_HANDLING -> Res.string.skill_animal_handling
    Skill.ARCANA -> Res.string.skill_arcana
    Skill.ATHLETICS -> Res.string.skill_athletics
    Skill.DECEPTION -> Res.string.skill_deception
    Skill.HISTORY -> Res.string.skill_history
    Skill.INSIGHT -> Res.string.skill_insight
    Skill.INTIMIDATION -> Res.string.skill_intimidation
    Skill.INVESTIGATION -> Res.string.skill_investigation
    Skill.MEDICINE -> Res.string.skill_medicine
    Skill.NATURE -> Res.string.skill_nature
    Skill.PERCEPTION -> Res.string.skill_perception
    Skill.PERFORMANCE -> Res.string.skill_performance
    Skill.PERSUASION -> Res.string.skill_persuasion
    Skill.RELIGION -> Res.string.skill_religion
    Skill.SLEIGHT_OF_HAND -> Res.string.skill_sleight_of_hand
    Skill.STEALTH -> Res.string.skill_stealth
    Skill.SURVIVAL -> Res.string.skill_survival
}

@Composable
fun Skill.toFormattedString(): String = stringResource(toStringRes())

@Composable
fun Skill.relatedAbilityAbbreviation(): String = getRelatedAbility().toAbbreviationString()
