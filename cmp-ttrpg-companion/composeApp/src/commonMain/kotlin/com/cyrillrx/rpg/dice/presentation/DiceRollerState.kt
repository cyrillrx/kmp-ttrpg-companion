package com.cyrillrx.rpg.dice.presentation

import com.cyrillrx.rpg.dice.domain.DiceComposition
import com.cyrillrx.rpg.dice.domain.DiceExpression
import com.cyrillrx.rpg.dice.domain.Die
import com.cyrillrx.rpg.dice.domain.RollPool

/** [rollCount] tells two identical rolls apart. */
data class DiceRollerState(
    val countByDie: Map<Die, Int> = emptyMap(),
    val modifier: Int = 0,
    val lastRoll: RollPool? = null,
    val rollCount: Int = 0,
) {
    fun diceCount(): Int = countByDie.values.sum()

    fun expression(): DiceExpression? = toDiceComposition().toExpression()

    fun toDiceComposition(): DiceComposition = DiceComposition(countByDie, modifier)
}
