package com.cyrillrx.rpg.core.presentation.component.dnd

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material.icons.filled.Forest
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Water
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.cyrillrx.rpg.creature.domain.Creature
import com.cyrillrx.rpg.creature.domain.Monster
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.creature_alignment_chaotic_evil
import rpg_companion.composeapp.generated.resources.creature_alignment_chaotic_good
import rpg_companion.composeapp.generated.resources.creature_alignment_chaotic_neutral
import rpg_companion.composeapp.generated.resources.creature_alignment_lawful_evil
import rpg_companion.composeapp.generated.resources.creature_alignment_lawful_good
import rpg_companion.composeapp.generated.resources.creature_alignment_lawful_neutral
import rpg_companion.composeapp.generated.resources.creature_alignment_neutral
import rpg_companion.composeapp.generated.resources.creature_alignment_neutral_evil
import rpg_companion.composeapp.generated.resources.creature_alignment_neutral_good
import rpg_companion.composeapp.generated.resources.creature_alignment_unknown
import rpg_companion.composeapp.generated.resources.creature_size_gargantuan
import rpg_companion.composeapp.generated.resources.creature_size_huge
import rpg_companion.composeapp.generated.resources.creature_size_large
import rpg_companion.composeapp.generated.resources.creature_size_medium
import rpg_companion.composeapp.generated.resources.creature_size_small
import rpg_companion.composeapp.generated.resources.creature_size_tiny
import rpg_companion.composeapp.generated.resources.creature_size_unknown
import rpg_companion.composeapp.generated.resources.creature_subtitle
import rpg_companion.composeapp.generated.resources.creature_type_aberration
import rpg_companion.composeapp.generated.resources.creature_type_beast
import rpg_companion.composeapp.generated.resources.creature_type_celestial
import rpg_companion.composeapp.generated.resources.creature_type_construct
import rpg_companion.composeapp.generated.resources.creature_type_dragon
import rpg_companion.composeapp.generated.resources.creature_type_elemental
import rpg_companion.composeapp.generated.resources.creature_type_fey
import rpg_companion.composeapp.generated.resources.creature_type_fiend
import rpg_companion.composeapp.generated.resources.creature_type_giant
import rpg_companion.composeapp.generated.resources.creature_type_humanoid
import rpg_companion.composeapp.generated.resources.creature_type_monstrosity
import rpg_companion.composeapp.generated.resources.creature_type_ooze
import rpg_companion.composeapp.generated.resources.creature_type_plant
import rpg_companion.composeapp.generated.resources.creature_type_undead
import rpg_companion.composeapp.generated.resources.creature_type_unknown

@Composable
fun Monster.Type.toFormattedString(): String {
    val stringRes = when (this) {
        Monster.Type.ABERRATION -> Res.string.creature_type_aberration
        Monster.Type.BEAST -> Res.string.creature_type_beast
        Monster.Type.CELESTIAL -> Res.string.creature_type_celestial
        Monster.Type.CONSTRUCT -> Res.string.creature_type_construct
        Monster.Type.DRAGON -> Res.string.creature_type_dragon
        Monster.Type.ELEMENTAL -> Res.string.creature_type_elemental
        Monster.Type.FEY -> Res.string.creature_type_fey
        Monster.Type.FIEND -> Res.string.creature_type_fiend
        Monster.Type.GIANT -> Res.string.creature_type_giant
        Monster.Type.HUMANOID -> Res.string.creature_type_humanoid
        Monster.Type.MONSTROSITY -> Res.string.creature_type_monstrosity
        Monster.Type.OOZE -> Res.string.creature_type_ooze
        Monster.Type.PLANT -> Res.string.creature_type_plant
        Monster.Type.UNDEAD -> Res.string.creature_type_undead
        Monster.Type.UNKNOWN -> Res.string.creature_type_unknown
    }
    return stringResource(stringRes)
}

fun Monster.Type.toIcon(): ImageVector = when (this) {
    Monster.Type.ABERRATION -> Icons.Filled.Psychology
    Monster.Type.BEAST -> Icons.Filled.Pets
    Monster.Type.CELESTIAL -> Icons.Filled.Bolt
    Monster.Type.CONSTRUCT -> Icons.Filled.SmartToy
    Monster.Type.DRAGON -> Icons.Filled.Whatshot
    Monster.Type.ELEMENTAL -> Icons.Filled.AcUnit
    Monster.Type.FEY -> Icons.Filled.Forest
    Monster.Type.FIEND -> Icons.Filled.Dangerous
    Monster.Type.GIANT -> Icons.Filled.Shield
    Monster.Type.HUMANOID -> Icons.Filled.Groups
    Monster.Type.MONSTROSITY -> Icons.Filled.BugReport
    Monster.Type.OOZE -> Icons.Filled.Water
    Monster.Type.PLANT -> Icons.Filled.Forest
    Monster.Type.UNDEAD -> Icons.Filled.Dangerous
    Monster.Type.UNKNOWN -> Icons.Filled.QuestionMark
}

@Composable
fun Monster.Type.getColor(): Color = when (this) {
    else -> MaterialTheme.colorScheme.primary
}

@Composable
fun Creature.Size.toFormattedString(): String {
    val stringRes = when (this) {
        Creature.Size.TINY -> Res.string.creature_size_tiny
        Creature.Size.SMALL -> Res.string.creature_size_small
        Creature.Size.MEDIUM -> Res.string.creature_size_medium
        Creature.Size.LARGE -> Res.string.creature_size_large
        Creature.Size.HUGE -> Res.string.creature_size_huge
        Creature.Size.GARGANTUAN -> Res.string.creature_size_gargantuan
        Creature.Size.UNKNOWN -> Res.string.creature_size_unknown
    }
    return stringResource(stringRes)
}

// Creature.Alignment

@Composable
fun Creature.Alignment.toFormattedString(): String {
    val stringRes = when (this) {
        Creature.Alignment.LAWFUL_GOOD -> Res.string.creature_alignment_lawful_good
        Creature.Alignment.NEUTRAL_GOOD -> Res.string.creature_alignment_neutral_good
        Creature.Alignment.CHAOTIC_GOOD -> Res.string.creature_alignment_chaotic_good
        Creature.Alignment.LAWFUL_NEUTRAL -> Res.string.creature_alignment_lawful_neutral
        Creature.Alignment.NEUTRAL -> Res.string.creature_alignment_neutral
        Creature.Alignment.CHAOTIC_NEUTRAL -> Res.string.creature_alignment_chaotic_neutral
        Creature.Alignment.LAWFUL_EVIL -> Res.string.creature_alignment_lawful_evil
        Creature.Alignment.NEUTRAL_EVIL -> Res.string.creature_alignment_neutral_evil
        Creature.Alignment.CHAOTIC_EVIL -> Res.string.creature_alignment_chaotic_evil
        Creature.Alignment.UNKNOWN -> Res.string.creature_alignment_unknown
    }
    return stringResource(stringRes)
}

@Composable
fun Monster.getSubtitle(): String = stringResource(
    Res.string.creature_subtitle,
    type.toFormattedString(),
    size.toFormattedString(),
    alignment.toFormattedString(),
)

fun Monster.toFormattedCR(): String = "CR ${formatCRValue(challengeRating)}"

fun formatCRValue(cr: Float): String = when (cr) {
    0f -> "0"
    0.125f -> "1/8"
    0.25f -> "1/4"
    0.5f -> "1/2"
    else -> if (cr == cr.toLong().toFloat()) cr.toLong().toString() else cr.toString()
}
