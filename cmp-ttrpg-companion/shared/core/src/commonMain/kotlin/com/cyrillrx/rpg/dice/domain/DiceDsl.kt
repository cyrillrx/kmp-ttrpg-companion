package com.cyrillrx.rpg.dice.domain

val Int.d4: DiceExpression get() = dice(Die.D4)
val Int.d6: DiceExpression get() = dice(Die.D6)
val Int.d8: DiceExpression get() = dice(Die.D8)
val Int.d10: DiceExpression get() = dice(Die.D10)
val Int.d12: DiceExpression get() = dice(Die.D12)
val Int.d20: DiceExpression get() = dice(Die.D20)
val Int.d100: DiceExpression get() = dice(Die.D100)

private fun Int.dice(die: Die): DiceExpression = DiceExpression(listOf(DiceTerm(this, die)))
