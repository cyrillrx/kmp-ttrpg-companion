package com.cyrillrx.rpg.dice.domain

import kotlin.random.Random

/** Rolls the die twice and keeps the higher roll. */
fun Die.rollWithAdvantage(random: Random = Random.Default): DoubleRoll = rollTwiceKeeping(random, KeepRule.HIGHER)

/** Rolls the die twice and keeps the lower roll. */
fun Die.rollWithDisadvantage(random: Random = Random.Default): DoubleRoll = rollTwiceKeeping(random, KeepRule.LOWER)

private fun Die.rollTwiceKeeping(random: Random, rule: KeepRule): DoubleRoll {
    val first = roll(random)
    val second = roll(random)
    return if (rule.keeps(first.value, over = second.value)) {
        DoubleRoll(kept = first, discarded = second, rule = rule)
    } else {
        DoubleRoll(kept = second, discarded = first, rule = rule)
    }
}
