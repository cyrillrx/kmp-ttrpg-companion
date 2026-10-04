package com.cyrillrx.rpg.character.data

import com.cyrillrx.core.data.Imported
import com.cyrillrx.core.data.defaultSerializer
import com.cyrillrx.core.domain.Result
import com.cyrillrx.rpg.character.data.api.ApiCharacterExport
import com.cyrillrx.rpg.character.data.api.CHARACTER_EXPORT_FORMAT_VERSION
import com.cyrillrx.rpg.character.data.api.ENTITY_TYPE_CHARACTER
import com.cyrillrx.rpg.character.domain.Character
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonElement

fun readCharacterExport(
    content: String,
): Result<Imported<Character, CharacterImportWarning>, CharacterFileImportError> {
    val json = content.parseJsonOrNull()
        ?: return Result.Failure(CharacterFileImportError.InvalidJson)
    val envelope = json.decodeEnvelopeOrNull()
        ?: return Result.Failure(CharacterFileImportError.MalformedEnvelope)
    val formatVersion = envelope.formatVersion
        ?: return Result.Failure(CharacterFileImportError.MalformedEnvelope)
    if (formatVersion > CHARACTER_EXPORT_FORMAT_VERSION) {
        return Result.Failure(CharacterFileImportError.UnsupportedFormatVersion(formatVersion))
    }
    val entityType = envelope.entityType
        ?: return Result.Failure(CharacterFileImportError.MalformedEnvelope)
    if (entityType != ENTITY_TYPE_CHARACTER) {
        return Result.Failure(CharacterFileImportError.UnsupportedEntityType(entityType))
    }
    val character = envelope.character
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

// The payload is decoded along with the envelope, so a wrongly typed character field lands here too:
// the serializer gives its path only inside a free-form message.
private fun JsonElement.decodeEnvelopeOrNull(): ApiCharacterExport? = try {
    defaultSerializer.decodeFromJsonElement(ApiCharacterExport.serializer(), this)
} catch (_: SerializationException) {
    null
}
