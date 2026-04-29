package com.cyrillrx.rpg.core.presentation.component.dnd

import androidx.compose.runtime.Composable
import com.cyrillrx.rpg.creature.domain.Creature
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
