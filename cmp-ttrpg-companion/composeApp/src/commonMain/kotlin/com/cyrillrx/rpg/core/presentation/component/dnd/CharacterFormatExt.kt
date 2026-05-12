package com.cyrillrx.rpg.core.presentation.component.dnd

import androidx.compose.runtime.Composable
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.Race
import com.cyrillrx.rpg.creature.domain.Language
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
import rpg_companion.composeapp.generated.resources.language_abyssal
import rpg_companion.composeapp.generated.resources.language_celestial
import rpg_companion.composeapp.generated.resources.language_common
import rpg_companion.composeapp.generated.resources.language_deep_speech
import rpg_companion.composeapp.generated.resources.language_draconic
import rpg_companion.composeapp.generated.resources.language_druidic
import rpg_companion.composeapp.generated.resources.language_dwarvish
import rpg_companion.composeapp.generated.resources.language_elvish
import rpg_companion.composeapp.generated.resources.language_giant
import rpg_companion.composeapp.generated.resources.language_gnomish
import rpg_companion.composeapp.generated.resources.language_goblin
import rpg_companion.composeapp.generated.resources.language_halfling
import rpg_companion.composeapp.generated.resources.language_infernal
import rpg_companion.composeapp.generated.resources.language_orc
import rpg_companion.composeapp.generated.resources.language_primordial
import rpg_companion.composeapp.generated.resources.language_sylvan
import rpg_companion.composeapp.generated.resources.language_thieves_cant
import rpg_companion.composeapp.generated.resources.language_undercommon
import rpg_companion.composeapp.generated.resources.race_tiefling

@Composable
fun Language.toFormattedString(): String {
    val stringRes = when (this) {
        Language.ABYSSAL -> Res.string.language_abyssal
        Language.CELESTIAL -> Res.string.language_celestial
        Language.COMMON -> Res.string.language_common
        Language.DEEP_SPEECH -> Res.string.language_deep_speech
        Language.DRACONIC -> Res.string.language_draconic
        Language.DRUIDIC -> Res.string.language_druidic
        Language.DWARVISH -> Res.string.language_dwarvish
        Language.ELVISH -> Res.string.language_elvish
        Language.GIANT -> Res.string.language_giant
        Language.GNOMISH -> Res.string.language_gnomish
        Language.GOBLIN -> Res.string.language_goblin
        Language.HALFLING -> Res.string.language_halfling
        Language.INFERNAL -> Res.string.language_infernal
        Language.ORC -> Res.string.language_orc
        Language.PRIMORDIAL -> Res.string.language_primordial
        Language.SYLVAN -> Res.string.language_sylvan
        Language.THIEVES_CANT -> Res.string.language_thieves_cant
        Language.UNDERCOMMON -> Res.string.language_undercommon
    }
    return stringResource(stringRes)
}

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
