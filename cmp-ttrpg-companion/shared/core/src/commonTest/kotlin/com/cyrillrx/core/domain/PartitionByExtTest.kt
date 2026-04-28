package com.cyrillrx.core.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PartitionByExtTest {
    private object TestError : Error

    @Test
    fun `partitionBy all successes returns all data and empty errors`() {
        val input = listOf(1, 2, 3)
        val (data, errors) = input.partitionBy<Int, Int, Error> { Result.Success(it * 10) }
        assertEquals(expected = listOf(10, 20, 30), actual = data)
        assertTrue(errors.isEmpty())
    }

    @Test
    fun `partitionBy all failures returns empty data and all errors`() {
        val input = listOf(1, 2, 3)
        val (data, errors) = input.partitionBy<Int, Int, Error> { Result.Failure(TestError) }
        assertTrue(data.isEmpty())
        assertEquals(expected = 3, actual = errors.size)
    }

    @Test
    fun `partitionBy mixed results splits correctly`() {
        val input = listOf(1, 2, 3, 4)
        val (data, errors) = input.partitionBy<Int, Int, Error> { n ->
            if (n % 2 == 0) Result.Success(n) else Result.Failure(TestError)
        }
        assertEquals(expected = listOf(2, 4), actual = data)
        assertEquals(expected = 2, actual = errors.size)
    }

    @Test
    fun `partitionBy empty list returns two empty lists`() {
        val (data, errors) = emptyList<Int>().partitionBy<Int, Int, Error> { Result.Success(it) }
        assertTrue(data.isEmpty())
        assertTrue(errors.isEmpty())
    }
}
