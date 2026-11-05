package com.cyrillrx.rpg.dice.presentation

import kotlinx.datetime.LocalDate

/** [hiddenUpTo] is the newest roll a clear hides until it is undone or done, if one is pending. */
data class DiceHistoryState(val body: Body = Body.Loading, val hiddenUpTo: Long? = null) {
    sealed interface Body {
        data object Loading : Body

        data object Empty : Body

        data object Error : Body

        /** [today] tells which of [days] reads as today or yesterday. */
        data class WithData(val days: List<RollDay>, val today: LocalDate) : Body
    }

    /** Stays [Body.WithData] once emptied, so the hidden roll can still be cleared with the rest. */
    fun without(rollId: Long?): DiceHistoryState {
        val withData = body as? Body.WithData
        if (withData == null || rollId == null) return this

        val days = withData.days
            .map { day -> day.copy(rolls = day.rolls.filterNot { it.id == rollId }) }
            .filter { day -> day.rolls.isNotEmpty() }
        return copy(body = withData.copy(days = days))
    }
}
