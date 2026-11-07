package com.cyrillrx.rpg.dice.domain

import kotlin.random.Random

fun Die.rollWithAdvantage(random: Random = Random.Default): DoubleRoll = rollTwiceKeeping(random, KeepRule.HIGHER)

fun Die.rollWithDisadvantage(random: Random = Random.Default): DoubleRoll = rollTwiceKeeping(random, KeepRule.LOWER)

internal fun Die.rollTwiceKeeping(random: Random, rule: KeepRule): DoubleRoll = rule.keepOne(roll(random), roll(random))

internal fun KeepRule.keepOne(first: SimpleRoll, second: SimpleRoll): DoubleRoll {
    val firstIsKept = keeps(first.value, over = second.value)
    return DoubleRoll(
        kept = if (firstIsKept) first else second,
        discarded = if (firstIsKept) second else first,
        rule = this,
    )
}
