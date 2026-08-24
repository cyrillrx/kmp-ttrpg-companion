package com.cyrillrx.rpg.core.presentation.format

import androidx.compose.ui.text.font.FontWeight
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.ClassLevels
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

/** Classes ordered by decreasing level, ties broken alphabetically on the localized name. */
fun ClassLevels.sortedByLevelThenName(nameOf: (Character.Class) -> String): List<Pair<Character.Class, Int>> {
    val comparator = compareByDescending<Map.Entry<Character.Class, Int>> { it.value }
        .thenBy { nameOf(it.key).localizedSortKey() }
    return entries.sortedWith(comparator).map { it.key to it.value }
}

fun ClassLevels.primaryClass(nameOf: (Character.Class) -> String): Character.Class =
    sortedByLevelThenName(nameOf).firstOrNull()?.first ?: Character.Class.UNKNOWN

/** Class names ordered by decreasing level, e.g. "Cleric/Ranger". Empty when only UNKNOWN is set. */
fun ClassLevels.toClassNames(nameOf: (Character.Class) -> String): String =
    sortedByLevelThenName(nameOf)
        .filterNot { (clazz, _) -> clazz == Character.Class.UNKNOWN }
        .joinToString("/") { (clazz, _) -> nameOf(clazz) }

/**
 * Class breakdown ordered by decreasing level, e.g. "Cleric 5 / Ranger 3". A lone class keeps its name
 * only, since its level is the total already shown next to it.
 */
fun ClassLevels.toClassBreakdown(nameOf: (Character.Class) -> String): String {
    val sorted = sortedByLevelThenName(nameOf)
    if (sorted.size == 1) return nameOf(sorted.first().first)
    return sorted.joinToString(" / ") { (clazz, level) -> "${nameOf(clazz)} $level" }
}
