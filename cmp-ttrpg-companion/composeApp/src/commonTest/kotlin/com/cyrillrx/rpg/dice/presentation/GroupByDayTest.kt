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

class GroupByDayTest {

    private val paris = TimeZone.of("Europe/Paris")

    @Test
    fun `rolls are grouped by the day they were made in the time zone newest first`() {
        val rolls = listOf(
            recorded(id = 3, rolledAt = "2026-11-02T08:30:00Z"),
            recorded(id = 2, rolledAt = "2026-11-01T23:10:00Z"),
            recorded(id = 1, rolledAt = "2026-11-01T22:50:00Z"),
        )

        val days = rolls.groupByDay(paris)

        assertEquals(
            listOf(LocalDate(year = 2026, month = 11, day = 2), LocalDate(year = 2026, month = 11, day = 1)),
            days.map {
                it.date
            },
        )
        assertEquals(listOf(listOf(3L, 2L), listOf(1L)), days.map { day -> day.rolls.map { it.id } })
        assertEquals(LocalTime(hour = 0, minute = 10), days.first().rolls.last().time)
    }

    private fun recorded(id: Long, rolledAt: String) =
        RecordedRoll(id, RollPool(listOf(SimpleRoll(Die.D6, value = 1))), Instant.parse(rolledAt))
}
