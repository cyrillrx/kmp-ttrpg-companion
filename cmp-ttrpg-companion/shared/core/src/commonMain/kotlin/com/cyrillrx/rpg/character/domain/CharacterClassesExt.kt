package com.cyrillrx.rpg.character.domain

/** Levels left before the character reaches [MAX_CHARACTER_LEVEL], the cap being on the total, not on each class. */
val Character.remainingLevels: Int get() = (MAX_CHARACTER_LEVEL - totalLevel).coerceAtLeast(0)

fun Character.canAddClass(): Boolean = remainingLevels >= MIN_CHARACTER_LEVEL

fun Character.maxLevelFor(clazz: Character.Class): Int = (classes[clazz] ?: 0) + remainingLevels

fun Character.withClassLevel(clazz: Character.Class, level: Int): Character {
    if (clazz !in classes) return this
    val coerced = level.coerceIn(MIN_CHARACTER_LEVEL, maxLevelFor(clazz))
    return copy(classes = classes + (clazz to coerced)).withRecomputedPrimary()
}

fun Character.withClassAdded(clazz: Character.Class): Character {
    if (clazz in classes || !canAddClass()) return this

    // Only the pristine "unspecified" default is replaced by the first real class; a levelled or
    // coexisting unknown class is a class of its own and is kept.
    val assigned = if (classes == DEFAULT_CLASS_LEVELS) classes - Character.Class.UNKNOWN else classes
    return copy(
        classes = assigned + (clazz to MIN_CHARACTER_LEVEL),
        primaryClass = if (assigned.isEmpty()) clazz else primaryClass,
    )
}

fun Character.withClassRemoved(clazz: Character.Class): Character {
    if (clazz !in classes) return this

    val remaining = classes - clazz
    if (remaining.isEmpty()) return copy(classes = DEFAULT_CLASS_LEVELS, primaryClass = Character.Class.UNKNOWN)

    return copy(
        classes = remaining,
        primaryClass = if (primaryClass == clazz) remaining.highestLevelClass() else primaryClass,
    )
}

/**
 * The primary class is the highest-level one.
 * A tie keeps the current primary.
 * Raising a class above the primary (or lowering the primary below another) is what reassigns it.
 */
private fun Character.withRecomputedPrimary(): Character =
    if ((classes[primaryClass] ?: 0) == classes.values.max()) {
        this
    } else {
        copy(primaryClass = classes.highestLevelClass())
    }

/** Ties are broken on the declaration order so that the reassignment does not depend on the app language. */
private fun ClassLevels.highestLevelClass(): Character.Class =
    entries.maxWith(compareBy({ it.value }, { -it.key.ordinal })).key
