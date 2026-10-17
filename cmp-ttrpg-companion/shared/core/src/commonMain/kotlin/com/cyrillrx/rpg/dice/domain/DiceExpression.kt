package com.cyrillrx.rpg.dice.domain

import com.cyrillrx.core.domain.Result
import com.cyrillrx.rpg.core.domain.toSignedString
import kotlin.random.Random

const val MAX_DICE_COUNT = 100
const val MAX_MODIFIER = 1000

data class DiceTerm(val count: Int, val die: Die) {
    init {
        require(count in 1..MAX_DICE_COUNT) { "Cannot roll $count $die" }
    }

    override fun toString(): String = "${count}d${die.sides}"
}

/** What to roll, such as `1d20 + 3d6 + 1d4 + 3`; [toString] writes it back as a dice notation. */
@ConsistentCopyVisibility
data class DiceExpression private constructor(private val countByDie: Map<Die, Int>, val modifier: Int) {
    init {
        require(countByDie.isNotEmpty()) { "Cannot roll an expression without dice" }
        require(countByDie.values.sum() <= MAX_DICE_COUNT) { "Cannot roll more than $MAX_DICE_COUNT dice" }
        require(modifier in -MAX_MODIFIER..MAX_MODIFIER) { "Cannot add a modifier of $modifier" }
    }

    val terms: List<DiceTerm> = countByDie.map { (die, count) -> DiceTerm(count, die) }

    constructor(terms: List<DiceTerm>, modifier: Int = 0) : this(terms.countByDieBiggestFirst(), modifier)

    // TODO(#320): combine without throwing past MAX_DICE_COUNT or MAX_MODIFIER
    operator fun plus(other: DiceExpression): DiceExpression =
        DiceExpression(terms + other.terms, modifier + other.modifier)

    operator fun plus(bonus: Int): DiceExpression = copy(modifier = modifier + bonus)

    operator fun minus(penalty: Int): DiceExpression = plus(-penalty)

    fun roll(random: Random = Random.Default): RollPool {
        val rolls = terms.flatMap { term -> List(term.count) { term.die.roll(random) } }
        return RollPool(rolls, modifier)
    }

    override fun toString(): String = buildString {
        append(terms.joinToString("+"))
        if (modifier != 0) append(modifier.toSignedString())
    }

    companion object {
        /** Reads a notation such as `2d6 + 1d4 + 3`; `d20` stands for `1d20`. */
        fun parse(notation: String): Result<DiceExpression, DiceNotationError> = readDiceNotation(notation)
    }
}

private fun List<DiceTerm>.countByDieBiggestFirst(): Map<Die, Int> = groupingBy { it.die }
    .fold(0) { count, term -> count + term.count }
    .entries
    .sortedByDescending { (die, _) -> die.sides }
    .associate { (die, count) -> die to count }
