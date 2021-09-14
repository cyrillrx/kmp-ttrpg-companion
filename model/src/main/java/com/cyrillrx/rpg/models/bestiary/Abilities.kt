package com.cyrillrx.rpg.models.bestiary

/**
 * @author Cyril Leroux
 *         Created on 10/03/2020.
 */
open class Abilities(
    val str: Int = DEFAULT_ABILITY_VALUE,
    val dex: Int = DEFAULT_ABILITY_VALUE,
    val con: Int = DEFAULT_ABILITY_VALUE,
    val int: Int = DEFAULT_ABILITY_VALUE,
    val wis: Int = DEFAULT_ABILITY_VALUE,
    val cha: Int = DEFAULT_ABILITY_VALUE
) {
    companion object {
        const val DEFAULT_ABILITY_VALUE = 10
    }
}