package com.cyrillrx.rpg.dice.domain

import kotlin.random.Random

class DiceRoller(private val random: Random = Random.Default) {
    fun roll(die: Die, mode: RollMode = RollMode.NORMAL): DieRoll {
        val results = List(mode.rollCount) { random.nextInt(1, die.sides + 1) }
        return DieRoll(die, results, mode.keptIndexIn(results))
    }
}
