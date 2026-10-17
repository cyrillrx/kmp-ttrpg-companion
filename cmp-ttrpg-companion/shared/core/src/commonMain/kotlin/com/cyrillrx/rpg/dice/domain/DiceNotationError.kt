package com.cyrillrx.rpg.dice.domain

import com.cyrillrx.core.domain.Error

sealed interface DiceNotationError : Error {
    data object Blank : DiceNotationError
    data object DanglingSign : DiceNotationError
    data class ZeroDice(val part: String) : DiceNotationError
    data class SubtractedDice(val part: String) : DiceNotationError
    data class UnsupportedDie(val die: String) : DiceNotationError
    data object NoDice : DiceNotationError
    data object TooManyDice : DiceNotationError
    data object ModifierOutOfRange : DiceNotationError
    data class Unreadable(val part: String) : DiceNotationError
}
