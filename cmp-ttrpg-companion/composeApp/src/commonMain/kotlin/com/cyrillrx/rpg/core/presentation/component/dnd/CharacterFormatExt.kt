package com.cyrillrx.rpg.core.presentation.component.dnd

import androidx.compose.runtime.Composable
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.Race
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.class_barbarian
import rpg_companion.composeapp.generated.resources.class_bard
import rpg_companion.composeapp.generated.resources.class_cleric
import rpg_companion.composeapp.generated.resources.class_druid
import rpg_companion.composeapp.generated.resources.class_fighter
import rpg_companion.composeapp.generated.resources.class_monk
import rpg_companion.composeapp.generated.resources.class_paladin
import rpg_companion.composeapp.generated.resources.class_ranger
import rpg_companion.composeapp.generated.resources.class_rogue
import rpg_companion.composeapp.generated.resources.class_sorcerer
import rpg_companion.composeapp.generated.resources.class_unknown
import rpg_companion.composeapp.generated.resources.class_warlock
import rpg_companion.composeapp.generated.resources.class_wizard
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

@Composable
fun Character.Class.toFormattedString(): String {
    val stringRes = when (this) {
        Character.Class.BARBARIAN -> Res.string.class_barbarian
        Character.Class.BARD -> Res.string.class_bard
        Character.Class.CLERIC -> Res.string.class_cleric
        Character.Class.DRUID -> Res.string.class_druid
        Character.Class.FIGHTER -> Res.string.class_fighter
        Character.Class.MONK -> Res.string.class_monk
        Character.Class.PALADIN -> Res.string.class_paladin
        Character.Class.RANGER -> Res.string.class_ranger
        Character.Class.ROGUE -> Res.string.class_rogue
        Character.Class.SORCERER -> Res.string.class_sorcerer
        Character.Class.WARLOCK -> Res.string.class_warlock
        Character.Class.WIZARD -> Res.string.class_wizard
        Character.Class.UNKNOWN -> Res.string.class_unknown
    }
    return stringResource(stringRes)
}
