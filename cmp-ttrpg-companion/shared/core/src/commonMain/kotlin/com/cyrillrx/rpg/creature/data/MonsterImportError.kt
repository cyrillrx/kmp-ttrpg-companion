package com.cyrillrx.rpg.creature.data

import com.cyrillrx.core.domain.Error

sealed interface MonsterImportError : Error {
    data object MissingId : MonsterImportError
    data class MissingSource(val id: String) : MonsterImportError
    data class MissingType(val id: String) : MonsterImportError
    data class UnknownType(val id: String, val raw: String) : MonsterImportError
    data class MissingSize(val id: String) : MonsterImportError
    data class UnknownSize(val id: String, val raw: String) : MonsterImportError
    data class MissingAlignment(val id: String) : MonsterImportError
    data class UnknownAlignment(val id: String, val raw: String) : MonsterImportError
    data class MissingAbilities(val id: String) : MonsterImportError
    data class MissingTranslations(val id: String) : MonsterImportError
    data class InvalidTranslation(val id: String, val locale: String) : MonsterImportError
}
