package com.cyrillrx.rpg.dice.domain

val Int.d4: DiceExpression get() = toDiceExpression(Die.D4)
val Int.d6: DiceExpression get() = toDiceExpression(Die.D6)
val Int.d8: DiceExpression get() = toDiceExpression(Die.D8)
val Int.d10: DiceExpression get() = toDiceExpression(Die.D10)
val Int.d12: DiceExpression get() = toDiceExpression(Die.D12)
val Int.d20: DiceExpression get() = toDiceExpression(Die.D20)
val Int.d100: DiceExpression get() = toDiceExpression(Die.D100)

operator fun Int.plus(expression: DiceExpression): DiceExpression = expression + this

private fun Int.toDiceExpression(die: Die): DiceExpression = DiceExpression(listOf(DiceTerm(this, die)))
