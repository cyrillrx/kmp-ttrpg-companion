package com.cyrillrx.rpg.core.presentation.component.dnd

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.cyrillrx.rpg.character.domain.Background
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.Language
import com.cyrillrx.rpg.character.domain.Race
import com.cyrillrx.rpg.creature.domain.Ability
import com.cyrillrx.rpg.creature.domain.Proficiency
import com.cyrillrx.rpg.creature.domain.Skill
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.ability_label_cha
import rpg_companion.composeapp.generated.resources.ability_label_con
import rpg_companion.composeapp.generated.resources.ability_label_dex
import rpg_companion.composeapp.generated.resources.ability_label_int
import rpg_companion.composeapp.generated.resources.ability_label_str
import rpg_companion.composeapp.generated.resources.ability_label_wis
import rpg_companion.composeapp.generated.resources.background_acolyte
import rpg_companion.composeapp.generated.resources.background_charlatan
import rpg_companion.composeapp.generated.resources.background_criminal
import rpg_companion.composeapp.generated.resources.background_entertainer
import rpg_companion.composeapp.generated.resources.background_folk_hero
import rpg_companion.composeapp.generated.resources.background_guild_artisan
import rpg_companion.composeapp.generated.resources.background_hermit
import rpg_companion.composeapp.generated.resources.background_noble
import rpg_companion.composeapp.generated.resources.background_outlander
import rpg_companion.composeapp.generated.resources.background_sage
import rpg_companion.composeapp.generated.resources.background_sailor
import rpg_companion.composeapp.generated.resources.background_soldier
import rpg_companion.composeapp.generated.resources.background_urchin
import rpg_companion.composeapp.generated.resources.class_artificer
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
import rpg_companion.composeapp.generated.resources.proficiency_expert
import rpg_companion.composeapp.generated.resources.proficiency_none
import rpg_companion.composeapp.generated.resources.proficiency_proficient
import rpg_companion.composeapp.generated.resources.race_dragonborn
import rpg_companion.composeapp.generated.resources.race_dwarf
import rpg_companion.composeapp.generated.resources.race_elf
import rpg_companion.composeapp.generated.resources.race_gnome
import rpg_companion.composeapp.generated.resources.race_half_elf
import rpg_companion.composeapp.generated.resources.race_half_orc
import rpg_companion.composeapp.generated.resources.race_halfling
import rpg_companion.composeapp.generated.resources.race_human
import rpg_companion.composeapp.generated.resources.race_tiefling
import rpg_companion.composeapp.generated.resources.skill_acrobatics
import rpg_companion.composeapp.generated.resources.skill_animal_handling
import rpg_companion.composeapp.generated.resources.skill_arcana
import rpg_companion.composeapp.generated.resources.skill_athletics
import rpg_companion.composeapp.generated.resources.skill_deception
import rpg_companion.composeapp.generated.resources.skill_history
import rpg_companion.composeapp.generated.resources.skill_insight
import rpg_companion.composeapp.generated.resources.skill_intimidation
import rpg_companion.composeapp.generated.resources.skill_investigation
import rpg_companion.composeapp.generated.resources.skill_medicine
import rpg_companion.composeapp.generated.resources.skill_nature
import rpg_companion.composeapp.generated.resources.skill_perception
import rpg_companion.composeapp.generated.resources.skill_performance
import rpg_companion.composeapp.generated.resources.skill_persuasion
import rpg_companion.composeapp.generated.resources.skill_religion
import rpg_companion.composeapp.generated.resources.skill_sleight_of_hand
import rpg_companion.composeapp.generated.resources.skill_stealth
import rpg_companion.composeapp.generated.resources.skill_survival

@Composable
fun Background?.toFormattedString(): String {
    this ?: return ""
    val stringRes = when (this) {
        Background.ACOLYTE -> Res.string.background_acolyte
        Background.CHARLATAN -> Res.string.background_charlatan
        Background.CRIMINAL -> Res.string.background_criminal
        Background.ENTERTAINER -> Res.string.background_entertainer
        Background.FOLK_HERO -> Res.string.background_folk_hero
        Background.GUILD_ARTISAN -> Res.string.background_guild_artisan
        Background.HERMIT -> Res.string.background_hermit
        Background.NOBLE -> Res.string.background_noble
        Background.OUTLANDER -> Res.string.background_outlander
        Background.SAGE -> Res.string.background_sage
        Background.SAILOR -> Res.string.background_sailor
        Background.SOLDIER -> Res.string.background_soldier
        Background.URCHIN -> Res.string.background_urchin
    }
    return stringResource(stringRes)
}

fun Character.Class.toSvgPath(): String = when (this) {
    Character.Class.ARTIFICER -> "drawable/class_artificer.svg"
    Character.Class.BARBARIAN -> "drawable/class_barbarian.svg"
    Character.Class.BARD -> "drawable/class_bard.svg"
    Character.Class.CLERIC -> "drawable/class_cleric.svg"
    Character.Class.DRUID -> "drawable/class_druid.svg"
    Character.Class.FIGHTER -> "drawable/class_warrior.svg"
    Character.Class.MONK -> "drawable/class_monk.svg"
    Character.Class.PALADIN -> "drawable/class_paladin.svg"
    Character.Class.RANGER -> "drawable/class_ranger.svg"
    Character.Class.ROGUE -> "drawable/class_rogue.svg"
    Character.Class.SORCERER -> "drawable/class_sorcerer.svg"
    Character.Class.WARLOCK -> "drawable/class_warlock.svg"
    Character.Class.WIZARD -> "drawable/class_wizard.svg"
    Character.Class.UNKNOWN -> "drawable/class_warrior.svg"
}

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
fun Proficiency.toFormattedString(): String {
    val stringRes = when (this) {
        Proficiency.NONE -> Res.string.proficiency_none
        Proficiency.PROFICIENT -> Res.string.proficiency_proficient
        Proficiency.EXPERT -> Res.string.proficiency_expert
    }
    return stringResource(stringRes)
}

@Composable
fun Proficiency.getColor(): Color = when (this) {
    Proficiency.NONE -> Color.Unspecified
    Proficiency.PROFICIENT -> MaterialTheme.colorScheme.primary
    Proficiency.EXPERT -> MaterialTheme.colorScheme.tertiary
}

@Composable
fun Skill.toFormattedString(): String {
    val stringRes = when (this) {
        Skill.ACROBATICS -> Res.string.skill_acrobatics
        Skill.ANIMAL_HANDLING -> Res.string.skill_animal_handling
        Skill.ARCANA -> Res.string.skill_arcana
        Skill.ATHLETICS -> Res.string.skill_athletics
        Skill.DECEPTION -> Res.string.skill_deception
        Skill.HISTORY -> Res.string.skill_history
        Skill.INSIGHT -> Res.string.skill_insight
        Skill.INTIMIDATION -> Res.string.skill_intimidation
        Skill.INVESTIGATION -> Res.string.skill_investigation
        Skill.MEDICINE -> Res.string.skill_medicine
        Skill.NATURE -> Res.string.skill_nature
        Skill.PERCEPTION -> Res.string.skill_perception
        Skill.PERFORMANCE -> Res.string.skill_performance
        Skill.PERSUASION -> Res.string.skill_persuasion
        Skill.RELIGION -> Res.string.skill_religion
        Skill.SLEIGHT_OF_HAND -> Res.string.skill_sleight_of_hand
        Skill.STEALTH -> Res.string.skill_stealth
        Skill.SURVIVAL -> Res.string.skill_survival
    }
    return stringResource(stringRes)
}

@Composable
fun Ability.toAbbreviationString(): String {
    val stringRes = when (this) {
        Ability.STRENGTH -> Res.string.ability_label_str
        Ability.DEXTERITY -> Res.string.ability_label_dex
        Ability.CONSTITUTION -> Res.string.ability_label_con
        Ability.INTELLIGENCE -> Res.string.ability_label_int
        Ability.WISDOM -> Res.string.ability_label_wis
        Ability.CHARISMA -> Res.string.ability_label_cha
    }
    return stringResource(stringRes)
}

@Composable
fun Skill.relatedAbilityAbbreviation(): String = getRelatedAbility().toAbbreviationString()

@Composable
fun Character.Class.toFormattedString(): String {
    val stringRes = when (this) {
        Character.Class.ARTIFICER -> Res.string.class_artificer
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
