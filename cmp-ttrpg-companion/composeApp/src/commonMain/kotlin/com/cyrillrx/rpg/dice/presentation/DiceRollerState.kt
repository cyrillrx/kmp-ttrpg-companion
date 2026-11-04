package com.cyrillrx.rpg.dice.presentation

import com.cyrillrx.rpg.dice.domain.DiceExpression
import com.cyrillrx.rpg.dice.domain.DiceTerm
import com.cyrillrx.rpg.dice.domain.Die
import com.cyrillrx.rpg.dice.domain.RecordedRoll
import com.cyrillrx.rpg.dice.domain.RollPool

const val PREVIOUS_ROLLS_SHOWN = 2

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

    fun previousRolls(): List<RecordedRoll> {
        val beforeLastRoll = if (isNewestTheLastRoll()) recentRolls.drop(1) else recentRolls
        return beforeLastRoll.take(PREVIOUS_ROLLS_SHOWN)
    }

    private fun isNewestTheLastRoll(): Boolean {
        val newest = recentRolls.firstOrNull() ?: return false

        return if (lastRollId != null) newest.id == lastRollId else newest.pool == lastRoll
    }
}
