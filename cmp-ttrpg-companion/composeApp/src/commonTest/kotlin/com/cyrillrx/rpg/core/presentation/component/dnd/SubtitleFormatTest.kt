package com.cyrillrx.rpg.core.presentation.component.dnd

import androidx.compose.ui.graphics.Color
import com.cyrillrx.rpg.core.presentation.component.buildSubtitle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

private val TYPE_COLOR = Color(0xFF112233)
private val SUBTITLE_COLOR = Color(0xFF445566)

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

    @Test
    fun `buildSubtitle appends the subtitle to the type`() {
        val subtitle = buildSubtitle(
            type = "Dragon",
            typeColor = TYPE_COLOR,
            subtitle = SUBTITLE_SEPARATOR + "Large" + SUBTITLE_SEPARATOR + "Lawful Evil",
            subtitleColor = SUBTITLE_COLOR,
        )
        assertEquals(expected = "Dragon · Large · Lawful Evil", actual = subtitle.text)
    }

    @Test
    fun `buildSubtitle appends the subtitle verbatim without inserting a separator`() {
        val subtitle = buildSubtitle(
            type = "Evocation",
            typeColor = TYPE_COLOR,
            subtitle = SUBTITLE_SEPARATOR + "1 action",
            subtitleColor = SUBTITLE_COLOR,
        )
        assertEquals(expected = "Evocation · 1 action", actual = subtitle.text)
    }

    @Test
    fun `buildSubtitle renders the type alone when the subtitle is null`() {
        val subtitle = buildSubtitle(
            type = "Weapon",
            typeColor = TYPE_COLOR,
            subtitle = null,
            subtitleColor = SUBTITLE_COLOR,
        )
        assertEquals(expected = "Weapon", actual = subtitle.text)
        assertEquals(expected = 1, actual = subtitle.spanStyles.size)
    }

    @Test
    fun `buildSubtitle colors the type and the subtitle distinctly`() {
        val subtitle = buildSubtitle(
            type = "Dragon",
            typeColor = TYPE_COLOR,
            subtitle = SUBTITLE_SEPARATOR + "Large",
            subtitleColor = SUBTITLE_COLOR,
        )

        val typeSpan = subtitle.spanStyles[0]
        assertEquals(expected = TYPE_COLOR, actual = typeSpan.item.color)
        assertEquals(expected = 0, actual = typeSpan.start)
        assertEquals(expected = "Dragon".length, actual = typeSpan.end)

        val subtitleSpan = subtitle.spanStyles[1]
        assertEquals(expected = SUBTITLE_COLOR, actual = subtitleSpan.item.color)
        assertEquals(expected = "Dragon".length, actual = subtitleSpan.start)
    }
}
