package com.cyrillrx.rpg.dice.presentation

import com.cyrillrx.rpg.dice.domain.DiceExpression
import com.cyrillrx.rpg.dice.domain.DiceTerm
import com.cyrillrx.rpg.dice.domain.Die
import com.cyrillrx.rpg.dice.domain.RollPool

/** The dice being composed and the modifier shared with direct d20 rolls, plus the result of the last roll. */
data class DiceRollerState(
    val countByDie: Map<Die, Int> = emptyMap(),
    val modifier: Int = 0,
    val lastRoll: RollPool? = null,
) {
    fun diceCount(): Int = countByDie.values.sum()

    /** What a tap on Roll would throw, such as `2d6 + 5`; null until a die is composed. */
    fun composition(): DiceExpression? = if (countByDie.isEmpty()) {
        null
    } else {
        DiceExpression(countByDie.map { (die, count) -> DiceTerm(count, die) }, modifier)
    }
}
