package com.cyrillrx.rpg.dice.presentation

import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

class RelativeToTest {

    @Test
    fun `a day reads relative to today`() {
        val today = LocalDate(year = 2026, month = 3, day = 1)

        assertEquals(RelativeDay.TODAY, today.relativeTo(today))
        assertEquals(RelativeDay.YESTERDAY, LocalDate(year = 2026, month = 2, day = 28).relativeTo(today))
        assertEquals(RelativeDay.EARLIER, LocalDate(year = 2026, month = 2, day = 27).relativeTo(today))
    }
}
