package com.cyrillrx.rpg.dice.domain

interface DiceCompositionRepository {
    /** The last saved composition, or an empty one when none was saved yet. */
    suspend fun load(): DiceComposition

    suspend fun save(composition: DiceComposition)
}
