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
import com.cyrillrx.rpg.core.presentation.theme.SchoolAbjuration
import com.cyrillrx.rpg.core.presentation.theme.SchoolConjuration
import com.cyrillrx.rpg.core.presentation.theme.SchoolDivination
import com.cyrillrx.rpg.core.presentation.theme.SchoolEnchantment
import com.cyrillrx.rpg.core.presentation.theme.SchoolEvocation
import com.cyrillrx.rpg.core.presentation.theme.SchoolIllusion
import com.cyrillrx.rpg.core.presentation.theme.SchoolNecromancy
import com.cyrillrx.rpg.core.presentation.theme.SchoolTransmutation
import com.cyrillrx.rpg.spell.domain.Spell

fun Spell.getColor(): Color = school.getColor()

fun Spell.School.getColor(): Color = when (this) {
    Spell.School.ABJURATION -> SchoolAbjuration
    Spell.School.CONJURATION -> SchoolConjuration
    Spell.School.DIVINATION -> SchoolDivination
    Spell.School.ENCHANTMENT -> SchoolEnchantment
    Spell.School.EVOCATION -> SchoolEvocation
    Spell.School.ILLUSION -> SchoolIllusion
    Spell.School.NECROMANCY -> SchoolNecromancy
    Spell.School.TRANSMUTATION -> SchoolTransmutation
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
