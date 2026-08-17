package com.cyrillrx.rpg.character.domain

typealias ClassLevels = Map<Character.Class, Int>

val ClassLevels.totalLevel: Int get() = values.sum()

val ClassLevels.primaryClass: Character.Class get() = maxByOrNull { it.value }?.key ?: Character.Class.UNKNOWN
