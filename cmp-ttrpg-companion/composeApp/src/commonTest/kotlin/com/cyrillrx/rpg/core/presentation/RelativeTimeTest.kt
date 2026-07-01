package com.cyrillrx.rpg.core.presentation

import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class RelativeTimeTest {

    private val now = Instant.fromEpochMilliseconds(1_700_000_000_000)

    private fun periodBefore(elapsed: Duration): RelativeTimePeriod? =
        (now - elapsed).getRelativeTimePeriod(now)

    @Test
    fun `epoch-zero sentinel returns null`() {
        assertNull(Instant.fromEpochMilliseconds(0).getRelativeTimePeriod(now))
    }

    @Test
    fun `less than a minute is Now`() {
        assertEquals(RelativeTimePeriod.Now, periodBefore(30.seconds))
    }

    @Test
    fun `minutes below an hour`() {
        assertEquals(RelativeTimePeriod.Minutes(5), periodBefore(5.minutes))
    }

    @Test
    fun `hours below a day`() {
        assertEquals(RelativeTimePeriod.Hours(2), periodBefore(2.hours))
    }

    @Test
    fun `days below a month`() {
        assertEquals(RelativeTimePeriod.Days(29), periodBefore(29.days))
    }

    @Test
    fun `thirty days rounds to one month`() {
        assertEquals(RelativeTimePeriod.Months(1), periodBefore(30.days))
    }

    @Test
    fun `months below a year`() {
        assertEquals(RelativeTimePeriod.Months(2), periodBefore(60.days))
    }

    @Test
    fun `359 days still reads as months`() {
        assertEquals(RelativeTimePeriod.Months(11), periodBefore(359.days))
    }

    @Test
    fun `360 days rounds up to one year`() {
        assertEquals(RelativeTimePeriod.Years(1), periodBefore(360.days))
    }

    @Test
    fun `multiple years`() {
        assertEquals(RelativeTimePeriod.Years(2), periodBefore(730.days))
    }
}
