package com.cyrillrx.rpg.core.presentation.component.dnd

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class SubtitleFormatTest {

    @Test
    fun `joinNonNull returns null when no segments`() {
        assertNull(joinNonNull())
    }

    @Test
    fun `joinNonNull returns null when every segment is null`() {
        assertNull(joinNonNull(null, null))
    }

    @Test
    fun `joinNonNull returns the single non-null segment unchanged`() {
        assertEquals(expected = " (longsword)", actual = joinNonNull(null, " (longsword)", null))
    }

    @Test
    fun `joinNonNull concatenates non-null segments without inserting a separator`() {
        val subtitle = joinNonNull(
            " (longsword)",
            SUBTITLE_SEPARATOR + "Large",
            SUBTITLE_SEPARATOR + "Lawful Evil",
        )
        assertEquals(expected = " (longsword) · Large · Lawful Evil", actual = subtitle)
    }

    @Test
    fun `joinNonNull skips null segments while preserving order`() {
        val subtitle = joinNonNull(
            null,
            SUBTITLE_SEPARATOR + "Large",
            null,
            SUBTITLE_SEPARATOR + "Lawful Evil",
        )
        assertEquals(expected = " · Large · Lawful Evil", actual = subtitle)
    }
}
