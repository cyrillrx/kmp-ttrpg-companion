package com.cyrillrx.rpg.dice.domain

import kotlin.random.Random

data class DiceTerm(val count: Int, val die: Die) {
    init {
        require(count >= 1) { "Cannot roll $count $die" }
    }
}

/** What to roll, such as `1d20 + 3d6 + 1d4 + 3`. */
@ConsistentCopyVisibility
data class DiceExpression private constructor(val terms: List<DiceTerm>, val modifier: Int) {
    init {
        require(terms.isNotEmpty()) { "Cannot roll an expression without dice" }
    }

    operator fun plus(other: DiceExpression): DiceExpression = invoke(terms + other.terms, modifier + other.modifier)

    operator fun plus(bonus: Int): DiceExpression = copy(modifier = modifier + bonus)

    operator fun minus(penalty: Int): DiceExpression = plus(-penalty)

    fun roll(random: Random = Random.Default): RollPool {
        val rolls = terms.flatMap { term -> List(term.count) { term.die.roll(random) } }
        return RollPool(rolls, modifier)
    }

    companion object {
        /** Groups the terms by die, biggest die first, so that two expressions rolling the same dice are equal. */
        operator fun invoke(terms: List<DiceTerm>, modifier: Int = 0): DiceExpression =
            DiceExpression(terms.groupedByDie(), modifier)

        private fun List<DiceTerm>.groupedByDie(): List<DiceTerm> = groupBy { it.die }
            .map { (die, terms) -> DiceTerm(terms.sumOf { it.count }, die) }
            .sortedByDescending { it.die.sides }
    }
}
