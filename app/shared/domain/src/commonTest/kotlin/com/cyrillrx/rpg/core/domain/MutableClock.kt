package com.cyrillrx.rpg.core.domain

import kotlin.time.Clock
import kotlin.time.Instant

/** Clock whose current instant is set by the test, so repository timestamps are deterministic. */
class MutableClock(var instant: Instant) : Clock {
    override fun now(): Instant = instant
}
