package com.cyrillrx.rpg.dice.presentation

import kotlinx.datetime.LocalTime
import kotlin.test.Test
import kotlin.test.assertEquals

class ToClockTimeTest {

    @Test
    fun `a clock time always reads with two digit hours and minutes`() {
        assertEquals("09:05", LocalTime(hour = 9, minute = 5).toClockTime())
        assertEquals("21:40", LocalTime(hour = 21, minute = 40).toClockTime())
    }
}
