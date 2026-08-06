package com.cyrillrx.rpg.core.presentation.component.dnd

import androidx.compose.runtime.Composable
import com.cyrillrx.rpg.creature.domain.Monster
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.monster_subtitle
import rpg_companion.composeapp.generated.resources.monster_type_aberration
import rpg_companion.composeapp.generated.resources.monster_type_beast
import rpg_companion.composeapp.generated.resources.monster_type_celestial
import rpg_companion.composeapp.generated.resources.monster_type_construct
import rpg_companion.composeapp.generated.resources.monster_type_dragon
import rpg_companion.composeapp.generated.resources.monster_type_elemental
import rpg_companion.composeapp.generated.resources.monster_type_fey
import rpg_companion.composeapp.generated.resources.monster_type_fiend
import rpg_companion.composeapp.generated.resources.monster_type_giant
import rpg_companion.composeapp.generated.resources.monster_type_humanoid
import rpg_companion.composeapp.generated.resources.monster_type_monstrosity
import rpg_companion.composeapp.generated.resources.monster_type_ooze
import rpg_companion.composeapp.generated.resources.monster_type_plant
import rpg_companion.composeapp.generated.resources.monster_type_swarm
import rpg_companion.composeapp.generated.resources.monster_type_undead
import rpg_companion.composeapp.generated.resources.monster_type_unknown

@Composable
fun Monster.Type.toFormattedString(): String {
    val stringRes = when (this) {
        Monster.Type.ABERRATION -> Res.string.monster_type_aberration
        Monster.Type.BEAST -> Res.string.monster_type_beast
        Monster.Type.CELESTIAL -> Res.string.monster_type_celestial
        Monster.Type.CONSTRUCT -> Res.string.monster_type_construct
        Monster.Type.DRAGON -> Res.string.monster_type_dragon
        Monster.Type.ELEMENTAL -> Res.string.monster_type_elemental
        Monster.Type.FEY -> Res.string.monster_type_fey
        Monster.Type.FIEND -> Res.string.monster_type_fiend
        Monster.Type.GIANT -> Res.string.monster_type_giant
        Monster.Type.HUMANOID -> Res.string.monster_type_humanoid
        Monster.Type.MONSTROSITY -> Res.string.monster_type_monstrosity
        Monster.Type.OOZE -> Res.string.monster_type_ooze
        Monster.Type.PLANT -> Res.string.monster_type_plant
        Monster.Type.SWARM -> Res.string.monster_type_swarm
        Monster.Type.UNDEAD -> Res.string.monster_type_undead
        Monster.Type.UNKNOWN -> Res.string.monster_type_unknown
    }
    return stringResource(stringRes)
}

@Composable
fun Set<Monster.Type>.toFormattedString(): String =
    map { it.toFormattedString() }.joinToString(" / ")

@Composable
fun Monster.getSubtitle(): String = stringResource(
    Res.string.monster_subtitle,
    types.toFormattedString(),
    size.toFormattedString(),
    alignment.toFormattedString(),
)
