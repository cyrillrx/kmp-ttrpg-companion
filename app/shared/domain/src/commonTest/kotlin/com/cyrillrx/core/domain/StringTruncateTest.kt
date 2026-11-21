package com.cyrillrx.core.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class StringTruncateTest {

    @Test
    fun `keeps a string within the maximum unchanged`() {
        assertEquals("abc", "abc".truncate(3))
    }

    @Test
    fun `cuts a string longer than the maximum`() {
        assertEquals("ab", "abcd".truncate(2))
    }

    @Test
    fun `drops an emoji the maximum would split`() {
        assertEquals("a", "a🐉".truncate(2))
    }
}
