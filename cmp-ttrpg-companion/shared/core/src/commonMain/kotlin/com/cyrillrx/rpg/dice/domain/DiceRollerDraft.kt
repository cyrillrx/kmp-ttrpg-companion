package com.cyrillrx.rpg.dice.domain

/** Throws [IllegalArgumentException] when a count, the dice total or the modifier is beyond its limit. */
data class DiceRollerDraft(val countByDie: Map<Die, Int> = emptyMap(), val modifier: Int = 0) {
    init {
        require(countByDie.values.all { it in 1..MAX_DICE_COUNT }) { "Cannot compose $countByDie" }
        require(countByDie.values.sum() <= MAX_DICE_COUNT) { "Cannot compose more than $MAX_DICE_COUNT dice" }
        require(modifier in MODIFIER_RANGE) { "Cannot compose a modifier of $modifier" }
    }

    /** Null when there is no die to roll. */
    fun toExpression(): DiceExpression? = if (countByDie.isEmpty()) {
        null
    } else {
        DiceExpression(countByDie.map { (die, count) -> DiceTerm(count, die) }, modifier)
    }
}
