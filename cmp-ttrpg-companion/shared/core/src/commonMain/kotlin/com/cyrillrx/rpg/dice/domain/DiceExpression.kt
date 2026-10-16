package com.cyrillrx.rpg.dice.domain

import kotlin.random.Random

data class DiceTerm(val count: Int, val die: Die) {
    init {
        require(count >= 1) { "Cannot roll $count $die" }
    }
}

/** What to roll, such as `2d6 + 1d4 + 3`. */
data class DiceExpression(val terms: List<DiceTerm>, val modifier: Int = 0) {
    operator fun plus(other: DiceExpression): DiceExpression =
        DiceExpression(terms + other.terms, modifier + other.modifier)

    operator fun plus(bonus: Int): DiceExpression = copy(modifier = modifier + bonus)

    operator fun minus(penalty: Int): DiceExpression = plus(-penalty)

    fun roll(random: Random = Random.Default): RollPool {
        val rolls = terms.flatMap { term -> List(term.count) { term.die.roll(random) } }
        return RollPool(rolls, modifier)
    }
}
