package com.cyrillrx.rpg.dice.domain

import kotlin.random.Random

fun Die.rollWithAdvantage(random: Random = Random.Default): DoubleRoll = rollTwiceKeeping(random, ::maxOf)

fun Die.rollWithDisadvantage(random: Random = Random.Default): DoubleRoll = rollTwiceKeeping(random, ::minOf)

private fun Die.rollTwiceKeeping(random: Random, keptValue: (Int, Int) -> Int): DoubleRoll {
    val first = roll(random)
    val second = roll(random)
    return if (keptValue(first.value, second.value) == first.value) {
        DoubleRoll(kept = first, discarded = second)
    } else {
        DoubleRoll(kept = second, discarded = first)
    }
}
