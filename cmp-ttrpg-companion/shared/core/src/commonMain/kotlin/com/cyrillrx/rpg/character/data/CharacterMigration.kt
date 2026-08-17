package com.cyrillrx.rpg.character.data

import com.cyrillrx.core.data.deserialize
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.coerceToValidCharacterLevel
import kotlinx.serialization.Serializable

internal fun String.deserializeCharacter(): Character {
    val character = deserialize<Character>()
    if (character.classes.isNotEmpty()) return character

    val legacy = deserialize<LegacyClassLevel>()
    val clazz = legacy.clazz
    return if (clazz == null || clazz == Character.Class.UNKNOWN) {
        character
    } else {
        character.copy(classes = mapOf(clazz to (legacy.level ?: 1).coerceToValidCharacterLevel()))
    }
}

@Serializable
private data class LegacyClassLevel(
    val clazz: Character.Class? = null,
    val level: Int? = null,
)
