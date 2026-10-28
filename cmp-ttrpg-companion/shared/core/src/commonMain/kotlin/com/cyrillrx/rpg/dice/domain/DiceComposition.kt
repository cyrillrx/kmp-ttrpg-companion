package com.cyrillrx.rpg.dice.domain

/** The dice and the modifier the roller is set to, kept from one launch to the next. */
data class DiceComposition(val countByDie: Map<Die, Int> = emptyMap(), val modifier: Int = 0) {
    fun toExpression(): DiceExpression? = if (countByDie.isEmpty()) {
        null
    } else {
        DiceExpression(countByDie.map { (die, count) -> DiceTerm(count, die) }, modifier)
    }
}
