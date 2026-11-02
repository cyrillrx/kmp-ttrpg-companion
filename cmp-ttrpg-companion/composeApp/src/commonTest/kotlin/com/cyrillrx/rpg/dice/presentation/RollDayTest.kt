package com.cyrillrx.rpg.dice.presentation

import com.cyrillrx.rpg.dice.domain.Die
import com.cyrillrx.rpg.dice.domain.RecordedRoll
import com.cyrillrx.rpg.dice.domain.RollPool
import com.cyrillrx.rpg.dice.domain.SimpleRoll
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Instant

class RollDayTest {

    private val paris = TimeZone.of("Europe/Paris")

    @Test
    fun `rolls are grouped by the day they were made in the time zone newest first`() {
        val rolls = listOf(
            recorded(id = 3, rolledAt = "2026-11-02T08:30:00Z"),
            recorded(id = 2, rolledAt = "2026-11-01T23:10:00Z"),
            recorded(id = 1, rolledAt = "2026-11-01T22:50:00Z"),
        )

        val days = rolls.groupByDay(paris)

        assertEquals(listOf(LocalDate(2026, 11, 2), LocalDate(2026, 11, 1)), days.map { it.date })
        assertEquals(listOf(listOf(3L, 2L), listOf(1L)), days.map { day -> day.rolls.map { it.id } })
        assertEquals(LocalTime(0, 10), days.first().rolls.last().time)
    }

    @Test
    fun `a day reads relative to today`() {
        val today = LocalDate(2026, 3, 1)

        assertEquals(RelativeDay.TODAY, today.relativeTo(today))
        assertEquals(RelativeDay.YESTERDAY, LocalDate(2026, 2, 28).relativeTo(today))
        assertEquals(RelativeDay.EARLIER, LocalDate(2026, 2, 27).relativeTo(today))
    }

    @Test
    fun `a clock time always reads with two digit hours and minutes`() {
        assertEquals("09:05", LocalTime(9, 5).toClockTime())
        assertEquals("21:40", LocalTime(21, 40).toClockTime())
    }

    private fun recorded(id: Long, rolledAt: String) =
        RecordedRoll(id, RollPool(listOf(SimpleRoll(Die.D6, value = 1))), Instant.parse(rolledAt))
}
