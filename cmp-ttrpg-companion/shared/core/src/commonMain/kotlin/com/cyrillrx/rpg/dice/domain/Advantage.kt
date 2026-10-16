package com.cyrillrx.rpg.dice.domain

import kotlin.random.Random

fun Die.rollWithAdvantage(random: Random = Random.Default): DoubleRoll =
    rollTwiceKeeping(random) { first, second -> first.value >= second.value }

fun Die.rollWithDisadvantage(random: Random = Random.Default): DoubleRoll =
    rollTwiceKeeping(random) { first, second -> first.value <= second.value }

private inline fun Die.rollTwiceKeeping(
    random: Random,
    keepsFirst: (first: SimpleRoll, second: SimpleRoll) -> Boolean,
): DoubleRoll {
    val first = roll(random)
    val second = roll(random)
    return if (keepsFirst(first, second)) DoubleRoll(first, second) else DoubleRoll(second, first)
}
