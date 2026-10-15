package com.cyrillrx.rpg.dice.domain

/**
 * Every result drawn for one die, and the one that counts: rolling with advantage or disadvantage
 * draws more than one.
 */
data class DieRoll(val die: Die, val results: List<Int>, val keptIndex: Int) {
    init {
        require(keptIndex in results.indices) { "keptIndex $keptIndex is outside results $results" }
    }

    val kept: Int get() = results[keptIndex]
}
