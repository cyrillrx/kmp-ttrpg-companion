package com.cyrillrx.rpg.dice.domain

import com.cyrillrx.rpg.character.domain.D20Check

data class RollContext(val characterId: String, val characterName: String, val check: D20Check)
