package com.cyrillrx.rpg.dice.data

import com.cyrillrx.rpg.dice.domain.DiceComposition
import com.cyrillrx.rpg.dice.domain.DiceCompositionRepository

class RamDiceCompositionRepository(private var composition: DiceComposition = DiceComposition()) :
    DiceCompositionRepository {

    override suspend fun load(): DiceComposition = composition

    override suspend fun save(composition: DiceComposition) {
        this.composition = composition
    }
}
