package com.cyrillrx.rpg.spell.data

import com.cyrillrx.core.domain.Error

sealed interface SpellImportError : Error {
    data object MissingId : SpellImportError
    data class MissingSource(val id: String) : SpellImportError
    data class MissingLevel(val id: String) : SpellImportError
    data class MissingConcentration(val id: String) : SpellImportError
    data class MissingRitual(val id: String) : SpellImportError
    data class MissingComponents(val id: String) : SpellImportError
    data class MissingAvailableClasses(val id: String) : SpellImportError
    data class UnknownClass(val id: String, val raw: String) : SpellImportError
    data class EmptyAvailableClasses(val id: String) : SpellImportError
    data class MissingTranslations(val id: String) : SpellImportError
    data class InvalidTranslation(val id: String, val locale: String) : SpellImportError
    data class UnknownSchool(val id: String, val raw: String) : SpellImportError
}
