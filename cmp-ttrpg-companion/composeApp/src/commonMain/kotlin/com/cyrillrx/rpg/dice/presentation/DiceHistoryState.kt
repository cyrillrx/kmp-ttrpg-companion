package com.cyrillrx.rpg.dice.presentation

import kotlinx.datetime.LocalDate

data class DiceHistoryState(val body: Body = Body.Loading, val hiddenUpTo: Long? = null) {
    sealed interface Body {
        data object Loading : Body

        data object Empty : Body

        data object Error : Body

        /** [today] tells which of [days] reads as today or yesterday. */
        data class WithData(val days: List<RollDay>, val today: LocalDate) : Body
    }

    fun without(rollId: Long?): DiceHistoryState {
        val withData = body as? Body.WithData
        if (withData == null || rollId == null) return this

        val days = withData.days
            .map { day -> day.copy(rolls = day.rolls.filterNot { it.id == rollId }) }
            .filter { day -> day.rolls.isNotEmpty() }
        return copy(body = withData.copy(days = days))
    }
}
