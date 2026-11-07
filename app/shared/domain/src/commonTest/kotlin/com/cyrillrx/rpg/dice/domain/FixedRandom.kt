package com.cyrillrx.rpg.dice.domain

import kotlin.random.Random

class FixedRandom(vararg values: Int) : Random() {
    private val values = ArrayDeque(values.toList())

    override fun nextBits(bitCount: Int): Int = error("FixedRandom only serves nextInt(from, until)")

    override fun nextInt(from: Int, until: Int): Int {
        val value = values.removeFirst()
        require(value in from until until) { "$value is outside [$from, $until)" }

        return value
    }
}
