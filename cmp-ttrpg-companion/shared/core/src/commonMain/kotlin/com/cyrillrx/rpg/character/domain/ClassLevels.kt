package com.cyrillrx.rpg.character.domain

typealias ClassLevels = Map<Character.Class, Int>

/** A character always has a class and a level; an unspecified class is [Character.Class.UNKNOWN] at level 1. */
val DEFAULT_CLASS_LEVELS: ClassLevels = mapOf(Character.Class.UNKNOWN to MIN_CHARACTER_LEVEL)

val ClassLevels.totalLevel: Int get() = values.sum()

fun ClassLevels.orDefault(): ClassLevels = ifEmpty { DEFAULT_CLASS_LEVELS }
