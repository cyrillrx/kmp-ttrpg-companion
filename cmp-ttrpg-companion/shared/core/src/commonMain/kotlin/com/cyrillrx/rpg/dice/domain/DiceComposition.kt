package com.cyrillrx.rpg.dice.domain

data class DiceComposition(val countByDie: Map<Die, Int> = emptyMap(), val modifier: Int = 0) {
    fun toExpression(): DiceExpression? = if (countByDie.isEmpty()) {
        null
    } else {
        DiceExpression(countByDie.map { (die, count) -> DiceTerm(count, die) }, modifier)
    }
}
