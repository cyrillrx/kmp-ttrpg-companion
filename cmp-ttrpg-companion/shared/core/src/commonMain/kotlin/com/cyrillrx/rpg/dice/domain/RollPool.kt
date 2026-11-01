package com.cyrillrx.rpg.dice.domain

data class RollPool(val rolls: List<DieRoll> = emptyList(), val modifier: Int = 0) {
    init {
        require(rolls.sumOf { it.toTerm().count } <= MAX_DICE_COUNT) { "Cannot pool more than $MAX_DICE_COUNT dice" }
        require(modifier in MODIFIER_RANGE) { "Cannot add a modifier of $modifier" }
    }

    fun total(): Int = rolls.sumOf { it.value } + modifier

    fun isEmpty(): Boolean = rolls.isEmpty()

    /** What the pool rolled, such as `2d20kh1 + 2d6 + 6`; null until a die is rolled. */
    fun toExpression(): DiceExpression? {
        if (isEmpty()) return null

        return DiceExpression(rolls.map(DieRoll::toTerm), modifier)
    }

    fun toNotation(): String? = toExpression()?.toNotation()

    /** The rolls grouped by die, from the biggest and each die's double rolls first, as the notation reads. */
    fun rollsByDie(): Map<Die, List<DieRoll>> = rolls
        .sortedWith(compareBy(termOrder, DieRoll::toTerm))
        .groupBy(DieRoll::die)
}
