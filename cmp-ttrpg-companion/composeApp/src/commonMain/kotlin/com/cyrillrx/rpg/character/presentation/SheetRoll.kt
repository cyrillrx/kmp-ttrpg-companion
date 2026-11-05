package com.cyrillrx.rpg.character.presentation

import com.cyrillrx.rpg.character.domain.D20Check
import com.cyrillrx.rpg.dice.domain.RollPool

/** [rollCount] tells two identical rolls apart. */
data class SheetRoll(val check: D20Check, val lastRoll: RollPool, val rollCount: Int)
