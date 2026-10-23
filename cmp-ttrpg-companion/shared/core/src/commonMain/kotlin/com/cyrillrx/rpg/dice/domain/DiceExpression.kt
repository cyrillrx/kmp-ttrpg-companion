package com.cyrillrx.rpg.dice.domain

import com.cyrillrx.core.domain.Result
import kotlin.random.Random

const val MAX_DICE_COUNT = 100
const val MAX_MODIFIER = 1000
val MODIFIER_RANGE = -MAX_MODIFIER..MAX_MODIFIER

internal val biggestDieFirst: Comparator<Die> = compareByDescending { it.sides }

data class DiceTerm(val count: Int, val die: Die) {
    init {
        require(count in 1..MAX_DICE_COUNT) { "Cannot roll $count $die" }
    }

    fun toNotation(): String = "${count}d${die.sides}"

    override fun toString(): String = toNotation()
}

/** What to roll, such as `1d20 + 3d6 + 1d4 + 3`. */
@ConsistentCopyVisibility
data class DiceExpression private constructor(private val countByDie: Map<Die, Int>, val modifier: Int) {
    init {
        require(countByDie.isNotEmpty()) { "Cannot roll an expression without dice" }
        require(countByDie.values.sum() <= MAX_DICE_COUNT) { "Cannot roll more than $MAX_DICE_COUNT dice" }
        require(modifier in MODIFIER_RANGE) { "Cannot add a modifier of $modifier" }
    }

    val terms: List<DiceTerm> = countByDie.map { (die, count) -> DiceTerm(count, die) }

    constructor(terms: List<DiceTerm>, modifier: Int = 0) : this(terms.countByDieBiggestFirst(), modifier)

    operator fun plus(other: DiceExpression): DiceExpression =
        DiceExpression(terms + other.terms, modifier + other.modifier)

    operator fun plus(bonus: Int): DiceExpression = copy(modifier = modifier + bonus)

    operator fun minus(penalty: Int): DiceExpression = plus(-penalty)

    fun combine(other: DiceExpression): Result<DiceExpression, DiceLimitError> =
        checkedDiceExpression(terms + other.terms, modifier.toLong() + other.modifier)

    fun withBonus(bonus: Int): Result<DiceExpression, DiceLimitError> =
        checkedDiceExpression(terms, modifier.toLong() + bonus)

    fun roll(random: Random = Random.Default): RollPool {
        val rolls = terms.flatMap { term -> List(term.count) { term.die.roll(random) } }
        return RollPool(rolls, modifier)
    }

    /** Writes the expression as a notation that [parse] reads back, from the biggest die. */
    fun toNotation(): String = buildString {
        append(terms.joinToString(" + ", transform = DiceTerm::toNotation))
        when {
            modifier > 0 -> append(" + ").append(modifier)
            modifier < 0 -> append(" - ").append(-modifier)
        }
    }

    override fun toString(): String = toNotation()

    companion object {
        /** Reads a notation such as `2d6 + 1d4 + 3`; `d20` stands for `1d20`. */
        fun parse(notation: String): Result<DiceExpression, DiceNotationError> = readDiceNotation(notation)
    }
}

internal fun checkedDiceExpression(terms: List<DiceTerm>, modifier: Long): Result<DiceExpression, DiceLimitError> =
    when {
        terms.sumOf { it.count } > MAX_DICE_COUNT -> Result.Failure(DiceNotationError.TooManyDice(MAX_DICE_COUNT))
        modifier !in MODIFIER_RANGE -> Result.Failure(DiceNotationError.ModifierOutOfRange(MAX_MODIFIER))
        else -> Result.Success(DiceExpression(terms, modifier.toInt()))
    }

private fun List<DiceTerm>.countByDieBiggestFirst(): Map<Die, Int> = groupingBy { it.die }
    .fold(0) { count, term -> count + term.count }
    .entries
    .sortedWith(compareBy(biggestDieFirst) { it.key })
    .associate { (die, count) -> die to count }
