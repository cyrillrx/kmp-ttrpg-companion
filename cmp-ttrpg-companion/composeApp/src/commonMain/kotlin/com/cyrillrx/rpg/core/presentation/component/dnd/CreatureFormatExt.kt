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
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.cyrillrx.rpg.creature.domain.Creature
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
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
fun Creature.Type.toFormattedString(): String {
    val stringRes = when (this) {
        Creature.Type.ABERRATION -> Res.string.creature_type_aberration
        Creature.Type.BEAST -> Res.string.creature_type_beast
        Creature.Type.CELESTIAL -> Res.string.creature_type_celestial
        Creature.Type.CONSTRUCT -> Res.string.creature_type_construct
        Creature.Type.DRAGON -> Res.string.creature_type_dragon
        Creature.Type.ELEMENTAL -> Res.string.creature_type_elemental
        Creature.Type.FEY -> Res.string.creature_type_fey
        Creature.Type.FIEND -> Res.string.creature_type_fiend
        Creature.Type.GIANT -> Res.string.creature_type_giant
        Creature.Type.HUMANOID -> Res.string.creature_type_humanoid
        Creature.Type.MONSTROSITY -> Res.string.creature_type_monstrosity
        Creature.Type.OOZE -> Res.string.creature_type_ooze
        Creature.Type.PLANT -> Res.string.creature_type_plant
        Creature.Type.UNDEAD -> Res.string.creature_type_undead
        Creature.Type.UNKNOWN -> Res.string.creature_type_unknown
    }
    return stringResource(stringRes)
}

fun Creature.Type.toIcon(): ImageVector = when (this) {
    Creature.Type.ABERRATION -> Icons.Filled.Psychology
    Creature.Type.BEAST -> Icons.Filled.Pets
    Creature.Type.CELESTIAL -> Icons.Filled.Bolt
    Creature.Type.CONSTRUCT -> Icons.Filled.SmartToy
    Creature.Type.DRAGON -> Icons.Filled.Whatshot
    Creature.Type.ELEMENTAL -> Icons.Filled.AcUnit
    Creature.Type.FEY -> Icons.Filled.Forest
    Creature.Type.FIEND -> Icons.Filled.Dangerous
    Creature.Type.GIANT -> Icons.Filled.Shield
    Creature.Type.HUMANOID -> Icons.Filled.Groups
    Creature.Type.MONSTROSITY -> Icons.Filled.BugReport
    Creature.Type.OOZE -> Icons.Filled.Water
    Creature.Type.PLANT -> Icons.Filled.Forest
    Creature.Type.UNDEAD -> Icons.Filled.Dangerous
    Creature.Type.UNKNOWN -> Icons.Filled.QuestionMark
}

private val dragonColor = Color(183, 28, 28)
private val undeadColor = Color(74, 20, 140)
private val fiendColor = Color(136, 14, 79)
private val celestialColor = Color(255, 179, 0)
private val beastColor = Color(46, 125, 50)
private val defaultColor = Color(55, 71, 79)

fun Creature.Type.getColor(): Color = when (this) {
    Creature.Type.DRAGON -> dragonColor
    Creature.Type.UNDEAD -> undeadColor
    Creature.Type.FIEND -> fiendColor
    Creature.Type.CELESTIAL -> celestialColor
    Creature.Type.BEAST -> beastColor
    else -> defaultColor
}

fun Float.toFormattedCR(): String = when (this) {
    0f -> "0"
    0.125f -> "1/8"
    0.25f -> "1/4"
    0.5f -> "1/2"
    else -> if (this == this.toLong().toFloat()) this.toLong().toString() else this.toString()
}
