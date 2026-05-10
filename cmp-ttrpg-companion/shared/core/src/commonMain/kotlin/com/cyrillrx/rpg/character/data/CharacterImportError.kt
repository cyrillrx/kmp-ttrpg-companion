package com.cyrillrx.rpg.character.data

import com.cyrillrx.core.domain.Error

sealed class CharacterImportError : Error {
    data object MissingId : CharacterImportError()

    data class MissingName(
        val id: String,
    ) : CharacterImportError()

    data class UnknownSize(
        val id: String,
        val value: String,
    ) : CharacterImportError()

    data class UnknownAlignment(
        val id: String,
        val value: String,
    ) : CharacterImportError()
}
