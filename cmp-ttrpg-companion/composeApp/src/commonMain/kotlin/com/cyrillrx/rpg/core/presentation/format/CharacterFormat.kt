package com.cyrillrx.rpg.core.presentation.format

import androidx.compose.ui.text.font.FontWeight
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.creature.domain.Proficiency

fun Character.Class.toSvgPath(): String = when (this) {
    Character.Class.ARTIFICER -> "drawable/class_artificer.svg"
    Character.Class.BARBARIAN -> "drawable/class_barbarian.svg"
    Character.Class.BARD -> "drawable/class_bard.svg"
    Character.Class.CLERIC -> "drawable/class_cleric.svg"
    Character.Class.DRUID -> "drawable/class_druid.svg"
    Character.Class.FIGHTER -> "drawable/class_fighter.svg"
    Character.Class.MONK -> "drawable/class_monk.svg"
    Character.Class.PALADIN -> "drawable/class_paladin.svg"
    Character.Class.RANGER -> "drawable/class_ranger.svg"
    Character.Class.ROGUE -> "drawable/class_rogue.svg"
    Character.Class.SORCERER -> "drawable/class_sorcerer.svg"
    Character.Class.WARLOCK -> "drawable/class_warlock.svg"
    Character.Class.WIZARD -> "drawable/class_wizard.svg"
    // UNKNOWN never reaches here: it is short-circuited to ClassIconState.Unknown ("?") in resolveClassIconState.
    Character.Class.UNKNOWN -> error("UNKNOWN has no class icon; it is rendered as a '?' fallback")
}

fun Proficiency.getFontWeight(): FontWeight = when (this) {
    Proficiency.NONE -> FontWeight.Normal
    Proficiency.PROFICIENT, Proficiency.EXPERT -> FontWeight.Bold
}
