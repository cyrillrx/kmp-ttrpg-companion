package com.cyrillrx.rpg.core.presentation.component.dnd

import androidx.compose.runtime.Composable
import com.cyrillrx.rpg.character.domain.Race
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.race_dragonborn
import rpg_companion.composeapp.generated.resources.race_dwarf
import rpg_companion.composeapp.generated.resources.race_elf
import rpg_companion.composeapp.generated.resources.race_gnome
import rpg_companion.composeapp.generated.resources.race_half_elf
import rpg_companion.composeapp.generated.resources.race_half_orc
import rpg_companion.composeapp.generated.resources.race_halfling
import rpg_companion.composeapp.generated.resources.race_human
import rpg_companion.composeapp.generated.resources.race_tiefling

@Composable
fun Race.toFormattedString(): String {
    val stringRes = when (this) {
        Race.HUMAN -> Res.string.race_human
        Race.ELF -> Res.string.race_elf
        Race.DWARF -> Res.string.race_dwarf
        Race.HALFLING -> Res.string.race_halfling
        Race.HALF_ELF -> Res.string.race_half_elf
        Race.HALF_ORC -> Res.string.race_half_orc
        Race.DRAGONBORN -> Res.string.race_dragonborn
        Race.GNOME -> Res.string.race_gnome
        Race.TIEFLING -> Res.string.race_tiefling
    }
    return stringResource(stringRes)
}
