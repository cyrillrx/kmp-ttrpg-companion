package com.cyrillrx.rpg.core.presentation.component.dnd

import androidx.compose.runtime.Composable
import com.cyrillrx.rpg.creature.domain.Creature
import com.cyrillrx.rpg.creature.domain.Speeds
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
import rpg_companion.composeapp.generated.resources.speed_label_burrow
import rpg_companion.composeapp.generated.resources.speed_label_climb
import rpg_companion.composeapp.generated.resources.speed_label_fly
import rpg_companion.composeapp.generated.resources.speed_label_hover
import rpg_companion.composeapp.generated.resources.speed_label_swim

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

private val FEET_TO_METERS: Map<Int, String> = mapOf(
    5 to "1,5", 10 to "3", 15 to "4,5", 20 to "6", 25 to "7,5",
    30 to "9", 40 to "12", 50 to "15", 60 to "18",
    80 to "24", 90 to "27", 120 to "36", 150 to "45",
)

private fun Int.toMetersDisplay(): String =
    FEET_TO_METERS[this] ?: run {
        val m = this * 3f / 10f
        if (m % 1f == 0f) m.toInt().toString() else m.toString().replace(".", ",")
    }

@Composable
fun Speeds.toFormattedString(useFeet: Boolean): String {
    val flyLabel = stringResource(Res.string.speed_label_fly)
    val swimLabel = stringResource(Res.string.speed_label_swim)
    val climbLabel = stringResource(Res.string.speed_label_climb)
    val burrowLabel = stringResource(Res.string.speed_label_burrow)
    val hoverLabel = stringResource(Res.string.speed_label_hover)

    fun Int.format() = if (useFeet) "$this ft." else "${toMetersDisplay()} m"

    return buildList {
        walk?.let { add(it.format()) }
        fly?.let {
            val suffix = if (hover) " ($hoverLabel)" else ""
            add("$flyLabel ${it.format()}$suffix")
        }
        swim?.let { add("$swimLabel ${it.format()}") }
        climb?.let { add("$climbLabel ${it.format()}") }
        burrow?.let { add("$burrowLabel ${it.format()}") }
    }.joinToString(", ")
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
