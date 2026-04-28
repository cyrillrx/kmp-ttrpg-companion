package com.cyrillrx.core.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PartitionExtTest {
    private object TestError : Error

    @Test
    fun `partition all successes returns all data and empty errors`() {
        val results = listOf<Result<Int, Error>>(
            Result.Success(1),
            Result.Success(2),
            Result.Success(3),
        )
        val (data, errors) = results.partition()
        assertEquals(expected = listOf(1, 2, 3), actual = data)
        assertTrue(errors.isEmpty())
    }

    @Test
    fun `partition all failures returns empty data and all errors`() {
        val results = listOf<Result<Int, Error>>(
            Result.Failure(TestError),
            Result.Failure(TestError),
        )
        val (data, errors) = results.partition()
        assertTrue(data.isEmpty())
        assertEquals(expected = 2, actual = errors.size)
    }

    @Test
    fun `partition mixed results splits correctly`() {
        val results = listOf<Result<Int, Error>>(
            Result.Success(1),
            Result.Failure(TestError),
            Result.Success(3),
        )
        val (data, errors) = results.partition()
        assertEquals(expected = listOf(1, 3), actual = data)
        assertEquals(expected = 1, actual = errors.size)
    }

    @Test
    fun `partition empty list returns two empty lists`() {
        val (data, errors) = emptyList<Result<Int, Error>>().partition()
        assertTrue(data.isEmpty())
        assertTrue(errors.isEmpty())
    }
}
