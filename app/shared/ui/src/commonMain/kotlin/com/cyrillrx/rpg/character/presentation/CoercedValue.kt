package com.cyrillrx.rpg.character.presentation

sealed interface CoercedValue {
    data class Numeric(val original: Int, val coerced: Int) : CoercedValue
    data class Distance(val originalFeet: Int, val coercedFeet: Int) : CoercedValue
}
