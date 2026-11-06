package com.cyrillrx.rpg.character.presentation

import com.cyrillrx.rpg.character.domain.D20Check
import com.cyrillrx.rpg.dice.domain.DoubleRoll
import com.cyrillrx.rpg.dice.domain.KeepRule
import com.cyrillrx.rpg.dice.domain.RollPool

data class SheetRoll(val check: D20Check, val lastRoll: RollPool, val rollCount: Int) {
    fun keepRule(): KeepRule? = (lastRoll.rolls.singleOrNull() as? DoubleRoll)?.rule
}
