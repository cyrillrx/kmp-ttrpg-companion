package com.cyrillrx.rpg.dice.domain

data class RollPool(val rolls: List<DieRoll> = emptyList(), val modifier: Int = 0) {
    init {
        require(rolls.size <= MAX_DICE_COUNT) { "Cannot pool more than $MAX_DICE_COUNT dice" }
        require(modifier in -MAX_MODIFIER..MAX_MODIFIER) { "Cannot add a modifier of $modifier" }
    }

    fun total(): Int = rolls.sumOf { it.value } + modifier

    fun isEmpty(): Boolean = rolls.isEmpty()

    /** What the pool rolled, such as `8d6+6`; null until a die is rolled. */
    fun toExpression(): DiceExpression? =
        if (isEmpty()) null else DiceExpression(rolls.map { DiceTerm(1, it.die) }, modifier)

    fun add(roll: DieRoll): RollPool = copy(rolls = rolls + roll)

    fun remove(index: Int): RollPool {
        require(index in rolls.indices) { "No roll at index $index in a pool of ${rolls.size}" }

        return copy(rolls = rolls.filterIndexed { i, _ -> i != index })
    }

    fun withModifier(modifier: Int): RollPool = copy(modifier = modifier)
}
