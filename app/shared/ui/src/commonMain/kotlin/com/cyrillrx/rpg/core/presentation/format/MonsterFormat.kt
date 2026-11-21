package com.cyrillrx.rpg.core.presentation.format

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material.icons.filled.Forest
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Water
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.cyrillrx.rpg.core.presentation.theme.MonsterTypeColors
import com.cyrillrx.rpg.creature.domain.Monster

fun Monster.Type.getIcon(): ImageVector = when (this) {
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
    Monster.Type.SWARM -> Icons.Filled.Grain
    Monster.Type.UNDEAD -> Icons.Filled.Dangerous
    Monster.Type.UNKNOWN -> Icons.Filled.QuestionMark
}

fun Monster.Type.getColor(): Color = when (this) {
    Monster.Type.ABERRATION -> MonsterTypeColors.aberration
    Monster.Type.BEAST -> MonsterTypeColors.beast
    Monster.Type.CELESTIAL -> MonsterTypeColors.celestial
    Monster.Type.CONSTRUCT -> MonsterTypeColors.construct
    Monster.Type.DRAGON -> MonsterTypeColors.dragon
    Monster.Type.ELEMENTAL -> MonsterTypeColors.elemental
    Monster.Type.FEY -> MonsterTypeColors.fey
    Monster.Type.FIEND -> MonsterTypeColors.fiend
    Monster.Type.GIANT -> MonsterTypeColors.giant
    Monster.Type.HUMANOID -> MonsterTypeColors.humanoid
    Monster.Type.MONSTROSITY -> MonsterTypeColors.monstrosity
    Monster.Type.OOZE -> MonsterTypeColors.ooze
    Monster.Type.PLANT -> MonsterTypeColors.plant
    Monster.Type.SWARM -> MonsterTypeColors.swarm
    Monster.Type.UNDEAD -> MonsterTypeColors.undead
    Monster.Type.UNKNOWN -> MonsterTypeColors.unknown
}

fun formatCRValue(cr: Float): String = when (cr) {
    0f -> "0"
    0.125f -> "1/8"
    0.25f -> "1/4"
    0.5f -> "1/2"
    else -> if (cr == cr.toLong().toFloat()) cr.toLong().toString() else cr.toString()
}
