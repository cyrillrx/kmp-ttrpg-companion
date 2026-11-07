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
import com.cyrillrx.rpg.core.presentation.theme.MonsterAberration
import com.cyrillrx.rpg.core.presentation.theme.MonsterBeast
import com.cyrillrx.rpg.core.presentation.theme.MonsterCelestial
import com.cyrillrx.rpg.core.presentation.theme.MonsterConstruct
import com.cyrillrx.rpg.core.presentation.theme.MonsterDragon
import com.cyrillrx.rpg.core.presentation.theme.MonsterElemental
import com.cyrillrx.rpg.core.presentation.theme.MonsterFey
import com.cyrillrx.rpg.core.presentation.theme.MonsterFiend
import com.cyrillrx.rpg.core.presentation.theme.MonsterGiant
import com.cyrillrx.rpg.core.presentation.theme.MonsterHumanoid
import com.cyrillrx.rpg.core.presentation.theme.MonsterMonstrosity
import com.cyrillrx.rpg.core.presentation.theme.MonsterOoze
import com.cyrillrx.rpg.core.presentation.theme.MonsterPlant
import com.cyrillrx.rpg.core.presentation.theme.MonsterSwarm
import com.cyrillrx.rpg.core.presentation.theme.MonsterUndead
import com.cyrillrx.rpg.core.presentation.theme.MonsterUnknown
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
    Monster.Type.ABERRATION -> MonsterAberration
    Monster.Type.BEAST -> MonsterBeast
    Monster.Type.CELESTIAL -> MonsterCelestial
    Monster.Type.CONSTRUCT -> MonsterConstruct
    Monster.Type.DRAGON -> MonsterDragon
    Monster.Type.ELEMENTAL -> MonsterElemental
    Monster.Type.FEY -> MonsterFey
    Monster.Type.FIEND -> MonsterFiend
    Monster.Type.GIANT -> MonsterGiant
    Monster.Type.HUMANOID -> MonsterHumanoid
    Monster.Type.MONSTROSITY -> MonsterMonstrosity
    Monster.Type.OOZE -> MonsterOoze
    Monster.Type.PLANT -> MonsterPlant
    Monster.Type.SWARM -> MonsterSwarm
    Monster.Type.UNDEAD -> MonsterUndead
    Monster.Type.UNKNOWN -> MonsterUnknown
}

fun Monster.toFormattedCR(): String = "CR ${formatCRValue(challengeRating)}"

fun formatCRValue(cr: Float): String = when (cr) {
    0f -> "0"
    0.125f -> "1/8"
    0.25f -> "1/4"
    0.5f -> "1/2"
    else -> if (cr == cr.toLong().toFloat()) cr.toLong().toString() else cr.toString()
}
