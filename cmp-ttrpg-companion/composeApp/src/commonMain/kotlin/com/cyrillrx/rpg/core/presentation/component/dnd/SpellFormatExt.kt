package com.cyrillrx.rpg.core.presentation.component.dnd

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Flare
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.Dangerous
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.cyrillrx.rpg.core.presentation.theme.SpellColor
import com.cyrillrx.rpg.spell.domain.Spell
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
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

fun Spell.getColor(): Color = SpellColor

@Composable
fun Spell.getFormattedSchool() =
    stringResource(Res.string.formatted_spell_school_level, getSchool(), level)

fun Spell.getFormattedComponents(): String = components.toFormattedString()

private fun Spell.Components.toFormattedString(): String = buildList {
    if (verbal) add("V")
    if (somatic) add("S")
    if (material) add("M")
}.joinToString(", ")

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

fun Spell.School?.toIcon(): ImageVector = when (this) {
    Spell.School.ABJURATION -> Icons.Filled.Shield
    Spell.School.CONJURATION -> Icons.Filled.Flare
    Spell.School.DIVINATION -> Icons.Filled.Visibility
    Spell.School.ENCHANTMENT -> Icons.Filled.Psychology
    Spell.School.EVOCATION -> Icons.Filled.Bolt
    Spell.School.ILLUSION -> Icons.Filled.AutoAwesome
    Spell.School.NECROMANCY -> Icons.Outlined.Dangerous
    Spell.School.TRANSMUTATION -> Icons.Filled.SwapHoriz
    null -> Icons.Filled.AutoAwesome
}

@Composable
fun Spell.toFormattedLevel(): String = stringResource(
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
