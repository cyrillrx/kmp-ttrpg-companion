package com.cyrillrx.rpg.dice.domain

import kotlin.random.Random

data class DiceTerm(val count: Int, val die: Die) {
    init {
        require(count >= 1) { "Cannot roll $count $die" }
    }
}

/** What to roll, such as `2d6 + 1d4 + 3`; rolling it gives a [RollPool]. */
data class DiceExpression(val terms: List<DiceTerm>, val modifier: Int = 0) {
    operator fun plus(other: DiceExpression): DiceExpression =
        DiceExpression(terms + other.terms, modifier + other.modifier)

    operator fun plus(modifier: Int): DiceExpression = copy(modifier = this.modifier + modifier)

    operator fun minus(modifier: Int): DiceExpression = plus(-modifier)

    fun roll(random: Random = Random.Default): RollPool {
        val rolls = terms.flatMap { term -> List(term.count) { term.die.roll(random) } }
        return RollPool(rolls, modifier)
    }
}
