package com.cyrillrx.rpg.dice.presentation

import com.cyrillrx.rpg.dice.domain.DiceExpression
import com.cyrillrx.rpg.dice.domain.DiceTerm
import com.cyrillrx.rpg.dice.domain.Die
import com.cyrillrx.rpg.dice.domain.RecordedRoll
import com.cyrillrx.rpg.dice.domain.RollPool

private const val PREVIOUS_ROLLS_SHOWN = 2

/**
 * [rollCount] tells two identical rolls apart; [lastRollId] is the id [lastRoll] was recorded under, once it is;
 * [recentRolls] are the newest recorded rolls, newest first.
 */
data class DiceRollerState(
    val countByDie: Map<Die, Int> = emptyMap(),
    val modifier: Int = 0,
    val lastRoll: RollPool? = null,
    val rollCount: Int = 0,
    val lastRollId: Long? = null,
    val recentRolls: List<RecordedRoll> = emptyList(),
) {
    fun diceCount(): Int = countByDie.values.sum()

    fun composition(): DiceExpression? = if (countByDie.isEmpty()) {
        null
    } else {
        DiceExpression(countByDie.map { (die, count) -> DiceTerm(count, die) }, modifier)
    }

    fun withLastRollRecorded(rollCount: Int, id: Long): DiceRollerState =
        if (rollCount == this.rollCount) copy(lastRollId = id) else this

    fun withHistory(rolls: List<RecordedRoll>): DiceRollerState {
        val recent = rolls.take(PREVIOUS_ROLLS_SHOWN + 1)
        if (lastRollId == null || rolls.any { it.id == lastRollId }) return copy(recentRolls = recent)

        return copy(lastRoll = null, lastRollId = null, recentRolls = recent)
    }

    /** A roll not recorded yet is newer than any clear, so it stays on the card. */
    fun hidingRollsUpTo(id: Long?): DiceRollerState {
        if (id == null || lastRollId == null || lastRollId > id) return this

        return copy(lastRoll = null, lastRollId = null)
    }

    fun previousRolls(): List<RecordedRoll> {
        val beforeLastRoll = if (isNewestTheLastRoll()) recentRolls.drop(1) else recentRolls
        return beforeLastRoll.take(PREVIOUS_ROLLS_SHOWN)
    }

    private fun isNewestTheLastRoll(): Boolean {
        val newest = recentRolls.firstOrNull() ?: return false

        // TODO(#361): give a roll its identity before it is recorded, so it is never matched by its result.
        return if (lastRollId == null) newest.pool == lastRoll else newest.id == lastRollId
    }
}
