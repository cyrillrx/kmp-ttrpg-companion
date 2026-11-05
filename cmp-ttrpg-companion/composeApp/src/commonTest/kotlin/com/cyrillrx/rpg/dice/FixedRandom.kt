package com.cyrillrx.rpg.dice

import kotlin.random.Random

internal class FixedRandom(vararg values: Int) : Random() {
    private val values = ArrayDeque(values.toList())

    override fun nextBits(bitCount: Int): Int = error("FixedRandom only serves nextInt(from, until)")

    override fun nextInt(from: Int, until: Int): Int = values.removeFirst()
}
