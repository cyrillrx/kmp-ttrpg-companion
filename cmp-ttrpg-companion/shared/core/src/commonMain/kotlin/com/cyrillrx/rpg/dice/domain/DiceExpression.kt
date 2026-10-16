package com.cyrillrx.rpg.dice.domain

import kotlin.random.Random

data class DiceTerm(val count: Int, val die: Die) {
    init {
        require(count >= 1) { "Cannot roll $count $die" }
    }
}

/** What to roll, such as `1d20 + 3d6 + 1d4 + 3`. */
@ConsistentCopyVisibility
data class DiceExpression private constructor(private val countByDie: Map<Die, Int>, val modifier: Int) {
    init {
        require(countByDie.isNotEmpty()) { "Cannot roll an expression without dice" }
    }

    val terms: List<DiceTerm> = countByDie
        .map { (die, count) -> DiceTerm(count, die) }
        .sortedByDescending { it.die.sides }

    operator fun plus(other: DiceExpression): DiceExpression = invoke(terms + other.terms, modifier + other.modifier)

    operator fun plus(bonus: Int): DiceExpression = copy(modifier = modifier + bonus)

    operator fun minus(penalty: Int): DiceExpression = plus(-penalty)

    fun roll(random: Random = Random.Default): RollPool {
        val rolls = terms.flatMap { term -> List(term.count) { term.die.roll(random) } }
        return RollPool(rolls, modifier)
    }

    companion object {
        operator fun invoke(terms: List<DiceTerm>, modifier: Int = 0): DiceExpression {
            val countByDie = terms.groupingBy { it.die }.fold(0) { count, term -> count + term.count }
            return DiceExpression(countByDie, modifier)
        }
    }
}
