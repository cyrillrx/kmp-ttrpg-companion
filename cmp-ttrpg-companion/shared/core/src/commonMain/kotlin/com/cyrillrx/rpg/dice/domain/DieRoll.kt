package com.cyrillrx.rpg.dice.domain

/** A die and the value that counts toward a total. */
sealed interface DieRoll {
    val die: Die
    val value: Int
}

data class SimpleRoll(override val die: Die, override val value: Int) : DieRoll

/** Two rolls of the same die, of which only [kept] counts. */
data class DoubleRoll(val kept: SimpleRoll, val discarded: SimpleRoll) : DieRoll by kept
