package com.cyrillrx.rpg.core.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class NumberExtTest {

    @Test
    fun `toSignedString prefixes positive number with plus`() {
        assertEquals(expected = "+2", actual = 2.toSignedString())
    }

    @Test
    fun `toSignedString prefixes zero with plus`() {
        assertEquals(expected = "+0", actual = 0.toSignedString())
    }

    @Test
    fun `toSignedString keeps minus sign for negative number`() {
        assertEquals(expected = "-1", actual = (-1).toSignedString())
    }
}
