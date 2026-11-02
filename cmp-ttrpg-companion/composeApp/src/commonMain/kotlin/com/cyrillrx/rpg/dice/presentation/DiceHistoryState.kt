package com.cyrillrx.rpg.dice.presentation

import kotlinx.datetime.LocalDate

sealed interface DiceHistoryState {
    data object Loading : DiceHistoryState

    data object Empty : DiceHistoryState

    data object Error : DiceHistoryState

    /** [today] tells which of [days] reads as today or yesterday. */
    data class WithData(val days: List<RollDay>, val today: LocalDate) : DiceHistoryState
}
