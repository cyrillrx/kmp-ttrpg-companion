package com.cyrillrx.rpg.character.data

sealed interface CharacterImportWarning {
    data class ValueCoerced(val id: String, val field: String, val declared: String, val kept: String) :
        CharacterImportWarning

    data class TranslationDropped(val cause: CharacterImportError.InvalidTranslation) : CharacterImportWarning

    data class UnknownBackground(val id: String, val value: String) : CharacterImportWarning
}
