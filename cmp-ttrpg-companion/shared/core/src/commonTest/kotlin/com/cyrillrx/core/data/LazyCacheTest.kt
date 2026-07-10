package com.cyrillrx.core.data

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.yield
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LazyCacheTest {

    @Test
    fun `loads once across multiple sequential gets`() = runTest {
        var loads = 0
        val cache = LazyCache {
            loads++
            "value"
        }

        assertEquals("value", cache.get())
        assertEquals("value", cache.get())
        assertEquals(1, loads)
    }

    @Test
    fun `concurrent gets trigger a single load`() = runTest {
        var loads = 0
        val cache = LazyCache {
            loads++
            yield()
            "value"
        }

        val results = (1..10).map { async { cache.get() } }.awaitAll()

        assertEquals(1, loads)
        assertTrue(results.all { it == "value" })
    }
}
