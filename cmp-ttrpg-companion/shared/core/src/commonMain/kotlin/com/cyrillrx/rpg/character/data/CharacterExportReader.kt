package com.cyrillrx.rpg.character.data

import com.cyrillrx.core.data.Imported
import com.cyrillrx.core.data.defaultSerializer
import com.cyrillrx.core.domain.Result
import com.cyrillrx.rpg.character.data.api.ApiCharacter
import com.cyrillrx.rpg.character.data.api.CHARACTER_EXPORT_FORMAT_VERSION
import com.cyrillrx.rpg.character.data.api.ENTITY_TYPE_CHARACTER
import com.cyrillrx.rpg.character.domain.Character
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull

fun readCharacterExport(
    content: String,
): Result<Imported<Character, CharacterImportWarning>, CharacterFileImportError> {
    val json = content.parseJsonOrNull()
        ?: return Result.Failure(CharacterFileImportError.InvalidJson)
    val envelope = json as? JsonObject
        ?: return Result.Failure(CharacterFileImportError.MalformedEnvelope)
    val formatVersion = envelope.primitive("formatVersion")?.intOrNull?.takeIf { it > 0 }
        ?: return Result.Failure(CharacterFileImportError.MalformedEnvelope)
    if (formatVersion > CHARACTER_EXPORT_FORMAT_VERSION) {
        return Result.Failure(CharacterFileImportError.UnsupportedFormatVersion(formatVersion))
    }
    val entityType = envelope.primitive("entityType")?.contentOrNull
        ?: return Result.Failure(CharacterFileImportError.MalformedEnvelope)
    if (entityType != ENTITY_TYPE_CHARACTER) {
        return Result.Failure(CharacterFileImportError.UnsupportedEntityType(entityType))
    }
    val character = envelope["character"]?.decodeCharacterOrNull()
        ?: return Result.Failure(CharacterFileImportError.MalformedEnvelope)
    return when (val result = character.toCharacter()) {
        is Result.Success -> result
        is Result.Failure -> Result.Failure(CharacterFileImportError.InvalidCharacter(result.error))
    }
}

private fun String.parseJsonOrNull(): JsonElement? = try {
    defaultSerializer.parseToJsonElement(this)
} catch (_: SerializationException) {
    null
}

private fun JsonObject.primitive(key: String): JsonPrimitive? = get(key) as? JsonPrimitive

// Decoded only once the version is known to be supported, so a newer format is refused rather than misreported.
// The serializer gives the path of a wrongly typed field only inside a free-form message.
private fun JsonElement.decodeCharacterOrNull(): ApiCharacter? = try {
    defaultSerializer.decodeFromJsonElement(ApiCharacter.serializer(), this)
} catch (_: SerializationException) {
    null
}
