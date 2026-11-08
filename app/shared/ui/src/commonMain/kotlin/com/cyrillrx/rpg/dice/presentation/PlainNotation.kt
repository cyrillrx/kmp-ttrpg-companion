package com.cyrillrx.rpg.dice.presentation

import com.cyrillrx.rpg.dice.domain.DoubleRoll
import com.cyrillrx.rpg.dice.domain.RollPool

/**
 * The pool's notation, with a lone double roll written as its kept die, such as `1d20 + 5` for `2d20kl1 + 5`;
 * null for an empty pool.
 */
fun RollPool.toPlainNotation(): String? {
    val doubleRoll = rolls.singleOrNull() as? DoubleRoll ?: return toNotation()

    return copy(rolls = listOf(doubleRoll.kept)).toNotation()
}
