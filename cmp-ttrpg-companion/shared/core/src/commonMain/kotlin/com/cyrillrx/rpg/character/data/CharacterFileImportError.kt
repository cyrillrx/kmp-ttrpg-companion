package com.cyrillrx.rpg.character.data

import com.cyrillrx.core.domain.Error

sealed interface CharacterFileImportError : Error {
    data object InvalidJson : CharacterFileImportError
    data object MalformedEnvelope : CharacterFileImportError
    data class UnsupportedFormatVersion(val version: Int) : CharacterFileImportError
    data class UnsupportedEntityType(val value: String) : CharacterFileImportError
    data class InvalidCharacter(val cause: CharacterImportError) : CharacterFileImportError
}
