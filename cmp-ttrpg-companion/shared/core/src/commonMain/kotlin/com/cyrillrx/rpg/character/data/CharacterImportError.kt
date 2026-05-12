package com.cyrillrx.rpg.character.data

import com.cyrillrx.core.domain.Error

sealed interface CharacterImportError : Error {
    data object MissingId : CharacterImportError

    data class MissingName(
        val id: String,
    ) : CharacterImportError

    data class MissingRace(
        val id: String,
    ) : CharacterImportError

    data class MissingClass(
        val id: String,
    ) : CharacterImportError

    data class MissingSize(
        val id: String,
    ) : CharacterImportError

    data class MissingAlignment(
        val id: String,
    ) : CharacterImportError

    data class MissingLevel(
        val id: String,
    ) : CharacterImportError

    data class MissingArmorClass(
        val id: String,
    ) : CharacterImportError

    data class MissingMaxHitPoints(
        val id: String,
    ) : CharacterImportError

    data class MissingSkills(
        val id: String,
    ) : CharacterImportError

    data class MissingTranslations(
        val id: String,
    ) : CharacterImportError

    data class UnknownRace(
        val id: String,
        val value: String,
    ) : CharacterImportError

    data class UnknownClass(
        val id: String,
        val value: String,
    ) : CharacterImportError

    data class UnknownSize(
        val id: String,
        val value: String,
    ) : CharacterImportError

    data class UnknownAlignment(
        val id: String,
        val value: String,
    ) : CharacterImportError

    data class UnknownLanguage(
        val id: String,
        val value: String,
    ) : CharacterImportError
}
