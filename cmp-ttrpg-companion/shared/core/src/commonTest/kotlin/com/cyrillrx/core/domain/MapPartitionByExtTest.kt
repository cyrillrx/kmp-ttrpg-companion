package com.cyrillrx.core.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private object TestError : Error

class MapPartitionByExtTest {

    @Test
    fun `partitionBy all successes returns full map and empty errors`() {
        val input = mapOf("a" to 1, "b" to 2, "c" to 3)
        val (result, errors) = input.partitionBy<String, Int, Int, Error> { _, v -> Result.Success(v * 10) }
        assertEquals(expected = mapOf("a" to 10, "b" to 20, "c" to 30), actual = result)
        assertTrue(errors.isEmpty())
    }

    @Test
    fun `partitionBy all failures returns empty map and all errors`() {
        val input = mapOf("a" to 1, "b" to 2)
        val (result, errors) = input.partitionBy<String, Int, Int, Error> { _, _ -> Result.Failure(TestError) }
        assertTrue(result.isEmpty())
        assertEquals(expected = 2, actual = errors.size)
    }

    @Test
    fun `partitionBy mixed results splits correctly`() {
        val input = mapOf("a" to 1, "b" to 2, "c" to 3, "d" to 4)
        val (result, errors) = input.partitionBy<String, Int, Int, Error> { _, v ->
            if (v % 2 == 0) Result.Success(v) else Result.Failure(TestError)
        }
        assertEquals(expected = mapOf("b" to 2, "d" to 4), actual = result)
        assertEquals(expected = 2, actual = errors.size)
    }

    @Test
    fun `partitionBy empty map returns empty map and empty errors`() {
        val (result, errors) = emptyMap<String, Int>().partitionBy<String, Int, Int, Error> { _, v -> Result.Success(v) }
        assertTrue(result.isEmpty())
        assertTrue(errors.isEmpty())
    }

    @Test
    fun `partitionBy key is preserved in result map`() {
        val input = mapOf("locale" to "value")
        val (result, _) = input.partitionBy<String, String, String, Error> { _, v -> Result.Success(v.uppercase()) }
        assertEquals(expected = "VALUE", actual = result["locale"])
    }

    @Test
    fun `partitionBy preserves insertion order of input map`() {
        val input = linkedMapOf("c" to 3, "a" to 1, "b" to 2)
        val (result, _) = input.partitionBy<String, Int, Int, Error> { _, v -> Result.Success(v) }
        assertEquals(expected = listOf("c", "a", "b"), actual = result.keys.toList())
    }
}
