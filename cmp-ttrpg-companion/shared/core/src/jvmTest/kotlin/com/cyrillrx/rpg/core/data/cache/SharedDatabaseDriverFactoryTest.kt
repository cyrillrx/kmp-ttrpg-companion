package com.cyrillrx.rpg.core.data.cache

import app.cash.sqldelight.db.SqlDriver
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class SharedDatabaseDriverFactoryTest {

    @Test
    fun `creates the driver once and returns the same instance across calls`() {
        var createCount = 0
        val delegate = object : DatabaseDriverFactory {
            override fun createDriver(): SqlDriver {
                createCount++
                return TestDatabaseDriverFactory().createDriver()
            }
        }

        val shared = SharedDatabaseDriverFactory(delegate)
        val first = shared.createDriver()
        val second = shared.createDriver()

        assertSame(first, second)
        assertEquals(expected = 1, actual = createCount)
    }
}
