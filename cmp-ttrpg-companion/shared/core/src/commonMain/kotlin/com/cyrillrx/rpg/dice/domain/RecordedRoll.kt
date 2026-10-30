package com.cyrillrx.rpg.dice.domain

import kotlin.time.Instant

data class RecordedRoll(val id: Long, val pool: RollPool, val rolledAt: Instant)
