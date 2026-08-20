package com.cyrillrx.rpg.character.data

import com.cyrillrx.core.data.defaultSerializer
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.MIN_CHARACTER_LEVEL
import com.cyrillrx.rpg.character.domain.coerceToValidCharacterLevel
import com.cyrillrx.rpg.character.domain.orDefault
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject

internal fun String.deserializeCharacter(): Character {
    val json = defaultSerializer.parseToJsonElement(this).jsonObject
    val character = defaultSerializer.decodeFromJsonElement<Character>(json)
    val legacy = defaultSerializer.decodeFromJsonElement<LegacyClassLevel>(json)
    val clazz = legacy.clazz ?: return character.copy(classes = character.classes.orDefault())
    val level = (legacy.level ?: MIN_CHARACTER_LEVEL).coerceToValidCharacterLevel()
    return character.copy(classes = mapOf(clazz to level))
}

@Serializable
private data class LegacyClassLevel(
    val clazz: Character.Class? = null,
    val level: Int? = null,
)
