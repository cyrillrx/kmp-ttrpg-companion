package com.cyrillrx.rpg.dice.domain

import com.cyrillrx.core.domain.Result

data class RollPool(val rolls: List<DieRoll> = emptyList(), val modifier: Int = 0) {
    init {
        require(rolls.size <= MAX_DICE_COUNT) { "Cannot pool more than $MAX_DICE_COUNT dice" }
        require(modifier in MODIFIER_RANGE) { "Cannot add a modifier of $modifier" }
    }

    fun total(): Int = rolls.sumOf { it.value } + modifier

    fun isEmpty(): Boolean = rolls.isEmpty()

    /**
     * Writes what the pool rolled, such as `1d20 + 2d6 + 6`, with [doubleRollNotation] writing each double roll
     * so that it reads apart from a plain roll; null until a die is rolled.
     */
    fun toNotation(doubleRollNotation: (DoubleRoll) -> String): String? {
        if (isEmpty()) return null

        val doubleRolls = rolls.filterIsInstance<DoubleRoll>().map(doubleRollNotation)
        val simpleTerms = rolls.filterIsInstance<SimpleRoll>()
            .map { DiceTerm(1, it.die) }
            .countByDieBiggestFirst()
            .map { (die, count) -> DiceTerm(count, die).toNotation() }
        return (doubleRolls + simpleTerms).joinToString(" + ") + modifierNotation(modifier)
    }

    /** The rolls grouped by die, from the biggest as the notation reads. */
    fun rollsByDie(): Map<Die, List<DieRoll>> = rolls.sortedWith(
        compareBy(biggestDieFirst) {
            it.die
        },
    ).groupBy { it.die }

    fun add(roll: DieRoll): Result<RollPool, DiceNotationError.TooManyDice> =
        if (rolls.size < MAX_DICE_COUNT) {
            Result.Success(copy(rolls = rolls + roll))
        } else {
            Result.Failure(DiceNotationError.TooManyDice(MAX_DICE_COUNT))
        }

    fun remove(index: Int): RollPool {
        require(index in rolls.indices) { "No roll at index $index in a pool of ${rolls.size}" }

        return copy(rolls = rolls.filterIndexed { i, _ -> i != index })
    }

    fun withModifier(modifier: Int): Result<RollPool, DiceNotationError.ModifierOutOfRange> =
        if (modifier in MODIFIER_RANGE) {
            Result.Success(copy(modifier = modifier))
        } else {
            Result.Failure(DiceNotationError.ModifierOutOfRange(MAX_MODIFIER))
        }
}
