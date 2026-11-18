package com.cyrillrx.rpg.core.presentation.format

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Flare
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.Dangerous
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.cyrillrx.rpg.core.presentation.theme.SpellSchoolColors
import com.cyrillrx.rpg.spell.domain.Spell

fun Spell.getColor(): Color = school.getColor()

fun Spell.School.getColor(): Color = when (this) {
    Spell.School.ABJURATION -> SpellSchoolColors.abjuration
    Spell.School.CONJURATION -> SpellSchoolColors.conjuration
    Spell.School.DIVINATION -> SpellSchoolColors.divination
    Spell.School.ENCHANTMENT -> SpellSchoolColors.enchantment
    Spell.School.EVOCATION -> SpellSchoolColors.evocation
    Spell.School.ILLUSION -> SpellSchoolColors.illusion
    Spell.School.NECROMANCY -> SpellSchoolColors.necromancy
    Spell.School.TRANSMUTATION -> SpellSchoolColors.transmutation
}

fun Spell.getFormattedComponents(): String = components.toFormattedString()

private fun Spell.Components.toFormattedString(): String = buildList {
    if (verbal) add("V")
    if (somatic) add("S")
    if (material) add("M")
}.joinToString(", ")

fun Spell.School?.getIcon(): ImageVector = when (this) {
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
