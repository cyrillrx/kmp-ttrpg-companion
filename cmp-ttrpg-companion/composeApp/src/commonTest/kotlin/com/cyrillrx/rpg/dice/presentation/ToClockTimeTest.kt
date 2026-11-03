package com.cyrillrx.rpg.dice.presentation

import kotlinx.datetime.LocalTime
import kotlin.test.Test
import kotlin.test.assertEquals

class ToClockTimeTest {

    @Test
    fun `a clock time always reads with two digit hours and minutes`() {
        assertEquals("09:05", LocalTime(9, 5).toClockTime())
        assertEquals("21:40", LocalTime(21, 40).toClockTime())
    }
}
