package com.cyrillrx.rpg.dice.domain

import com.cyrillrx.core.domain.Result

data class RollPool(val rolls: List<DieRoll> = emptyList(), val modifier: Int = 0) {
    init {
        require(rolls.size <= MAX_DICE_COUNT) { "Cannot pool more than $MAX_DICE_COUNT dice" }
        require(modifier in MODIFIER_RANGE) { "Cannot add a modifier of $modifier" }
    }

    fun total(): Int = rolls.sumOf { it.value } + modifier

    fun isEmpty(): Boolean = rolls.isEmpty()

    /** Writes what the pool rolled from the biggest die, such as `2d20kh1 + 2d6 + 6`; null until a die is rolled. */
    fun toNotation(): String? {
        if (isEmpty()) return null

        val terms = rollsByDie().flatMap { (die, rolls) ->
            val simpleCount = rolls.count { it is SimpleRoll }
            val simpleTerm = if (simpleCount > 0) listOf(DiceTerm(simpleCount, die).toNotation()) else emptyList()
            rolls.filterIsInstance<DoubleRoll>().map(DoubleRoll::toNotation) + simpleTerm
        }

        return terms.joinToString(TERM_SEPARATOR) + modifier.toModifierNotation()
    }

    /** The rolls grouped by die, from the biggest and each die's double rolls first, as the notation reads. */
    fun rollsByDie(): Map<Die, List<DieRoll>> = rolls
        .sortedWith(compareBy(biggestDieFirst, DieRoll::die).thenBy { it !is DoubleRoll })
        .groupBy(DieRoll::die)

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
