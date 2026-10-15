package com.cyrillrx.rpg.dice.domain

enum class RollMode(internal val rollCount: Int) {
    NORMAL(1),
    ADVANTAGE(2),
    DISADVANTAGE(2),
}

internal fun RollMode.keptIndexIn(results: List<Int>): Int = when (this) {
    RollMode.NORMAL -> 0
    RollMode.ADVANTAGE -> results.indices.maxBy { results[it] }
    RollMode.DISADVANTAGE -> results.indices.minBy { results[it] }
}
