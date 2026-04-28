package com.cyrillrx.rpg.magicalitem.data

import com.cyrillrx.core.domain.Error

sealed interface MagicalItemImportError : Error {
    data object MissingId : MagicalItemImportError
    data class MissingSource(val id: String) : MagicalItemImportError
    data class MissingType(val id: String) : MagicalItemImportError
    data class UnknownType(val id: String, val raw: String) : MagicalItemImportError
    data class MissingRarity(val id: String) : MagicalItemImportError
    data class UnknownRarity(val id: String, val raw: String) : MagicalItemImportError
    data class MissingTranslations(val id: String) : MagicalItemImportError
    data class InvalidTranslation(val id: String, val locale: String) : MagicalItemImportError
}
