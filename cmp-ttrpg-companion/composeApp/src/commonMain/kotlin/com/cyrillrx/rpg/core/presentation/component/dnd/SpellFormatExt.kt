package com.cyrillrx.rpg.core.presentation.component.dnd

import androidx.compose.runtime.Composable
import com.cyrillrx.rpg.spell.domain.Spell
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.component_material
import rpg_companion.composeapp.generated.resources.component_somatic
import rpg_companion.composeapp.generated.resources.component_verbal
import rpg_companion.composeapp.generated.resources.formatted_spell_school_level
import rpg_companion.composeapp.generated.resources.school_abjuration
import rpg_companion.composeapp.generated.resources.school_conjuration
import rpg_companion.composeapp.generated.resources.school_divination
import rpg_companion.composeapp.generated.resources.school_enchantment
import rpg_companion.composeapp.generated.resources.school_evocation
import rpg_companion.composeapp.generated.resources.school_illusion
import rpg_companion.composeapp.generated.resources.school_necromancy
import rpg_companion.composeapp.generated.resources.school_transmutation
import rpg_companion.composeapp.generated.resources.spell_level_1st
import rpg_companion.composeapp.generated.resources.spell_level_2nd
import rpg_companion.composeapp.generated.resources.spell_level_3rd
import rpg_companion.composeapp.generated.resources.spell_level_4th
import rpg_companion.composeapp.generated.resources.spell_level_5th
import rpg_companion.composeapp.generated.resources.spell_level_6th
import rpg_companion.composeapp.generated.resources.spell_level_7th
import rpg_companion.composeapp.generated.resources.spell_level_8th
import rpg_companion.composeapp.generated.resources.spell_level_9th
import rpg_companion.composeapp.generated.resources.spell_level_cantrip

@Composable
fun Spell.getFormattedSchoolAndLevel() =
    stringResource(Res.string.formatted_spell_school_level, getSchool(), level)

@Composable
fun Spell.ComponentType.toFormattedString(): String {
    val stringRes = when (this) {
        Spell.ComponentType.VERBAL -> Res.string.component_verbal
        Spell.ComponentType.SOMATIC -> Res.string.component_somatic
        Spell.ComponentType.MATERIAL -> Res.string.component_material
    }
    return stringResource(stringRes)
}

@Composable
fun Spell.getSchool(): String = school.toFormattedString()

@Composable
fun Spell.School.toFormattedString(): String {
    val stringRes = when (this) {
        Spell.School.ABJURATION -> Res.string.school_abjuration
        Spell.School.CONJURATION -> Res.string.school_conjuration
        Spell.School.DIVINATION -> Res.string.school_divination
        Spell.School.ENCHANTMENT -> Res.string.school_enchantment
        Spell.School.EVOCATION -> Res.string.school_evocation
        Spell.School.ILLUSION -> Res.string.school_illusion
        Spell.School.NECROMANCY -> Res.string.school_necromancy
        Spell.School.TRANSMUTATION -> Res.string.school_transmutation
    }
    return stringResource(stringRes)
}

@Composable
fun Spell.getFormattedLevel(): String = stringResource(
    when (level) {
        0 -> Res.string.spell_level_cantrip
        1 -> Res.string.spell_level_1st
        2 -> Res.string.spell_level_2nd
        3 -> Res.string.spell_level_3rd
        4 -> Res.string.spell_level_4th
        5 -> Res.string.spell_level_5th
        6 -> Res.string.spell_level_6th
        7 -> Res.string.spell_level_7th
        8 -> Res.string.spell_level_8th
        else -> Res.string.spell_level_9th
    },
)
