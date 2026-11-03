package com.cyrillrx.rpg.dice.presentation

import kotlinx.datetime.LocalDate

data class DiceHistoryState(val body: Body = Body.Loading) {
    sealed interface Body {
        data object Loading : Body

        data object Empty : Body

        data object Error : Body

        /** [today] tells which of [days] reads as today or yesterday. */
        data class WithData(val days: List<RollDay>, val today: LocalDate) : Body
    }
}
