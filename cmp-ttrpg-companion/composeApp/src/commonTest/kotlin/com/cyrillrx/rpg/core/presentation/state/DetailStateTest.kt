package com.cyrillrx.rpg.core.presentation.state

import kotlin.test.Test
import kotlin.test.assertEquals

class DetailStateTest {

    @Test
    fun `DetailState of something returns Found`() {
        assertEquals(
            expected = DetailState.Found("some-item"),
            actual = DetailState.of("1", "some-item"),
        )
    }

    @Test
    fun `DetailState of null returns NotFound`() {
        assertEquals(
            expected = DetailState.NotFound("1"),
            actual = DetailState.of<String>("1", null),
        )
    }
}
