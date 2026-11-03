package com.cyrillrx.rpg.dice.presentation

import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

class RelativeToTest {

    @Test
    fun `a day reads relative to today`() {
        val today = LocalDate(2026, 3, 1)

        assertEquals(RelativeDay.TODAY, today.relativeTo(today))
        assertEquals(RelativeDay.YESTERDAY, LocalDate(2026, 2, 28).relativeTo(today))
        assertEquals(RelativeDay.EARLIER, LocalDate(2026, 2, 27).relativeTo(today))
    }
}
