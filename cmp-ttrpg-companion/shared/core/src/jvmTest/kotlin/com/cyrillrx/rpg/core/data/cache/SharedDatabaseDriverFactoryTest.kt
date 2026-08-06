package com.cyrillrx.rpg.core.data.cache

import app.cash.sqldelight.db.SqlDriver
import com.cyrillrx.rpg.character.data.SQLDelightCharacterRepository
import com.cyrillrx.rpg.character.data.SampleCharacterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

class SharedDatabaseDriverFactoryTest {

    @Test
    fun `creates the driver once and returns the same instance across calls`() {
        val delegate = CountingDriverFactory()

        val shared = SharedDatabaseDriverFactory(delegate)
        val first = shared.createDriver()
        val second = shared.createDriver()

        assertSame(first, second)
        assertEquals(expected = 1, actual = delegate.createCount)
    }

    @Test
    fun `creates the driver once when called concurrently`() = runTest {
        val delegate = CountingDriverFactory()

        val shared = SharedDatabaseDriverFactory(delegate)
        val drivers = withContext(Dispatchers.Default) {
            List(CONCURRENT_CALLS) { async { shared.createDriver() } }.awaitAll()
        }

        assertEquals(expected = 1, actual = drivers.distinct().size)
        assertEquals(expected = 1, actual = delegate.createCount)
    }

    @Test
    fun `does not open the driver until a repository asks for it`() {
        val delegate = CountingDriverFactory()

        SharedDatabaseDriverFactory(delegate)

        assertEquals(expected = 0, actual = delegate.createCount)
    }

    @Test
    fun `repositories built from the same factory share the driver`() = runTest {
        // Each in-memory driver owns its own database, so reading back a character written through
        // another repository can only succeed when both repositories got the very same driver.
        val shared = SharedDatabaseDriverFactory(TestDatabaseDriverFactory())
        val writer = SQLDelightCharacterRepository(shared)
        val reader = SQLDelightCharacterRepository(shared)
        val fighter = SampleCharacterRepository.humanFighter()

        writer.save(fighter)

        assertEquals(fighter, reader.get(fighter.id))
    }

    @Test
    fun `close releases the shared driver`() {
        val driver = CloseTrackingDriver(TestDatabaseDriverFactory().createDriver())
        val shared = SharedDatabaseDriverFactory(
            object : DatabaseDriverFactory {
                override fun createDriver() = driver
            },
        )
        shared.createDriver()

        shared.close()

        assertTrue(driver.closed)
    }

    @Test
    fun `close does not open a driver that was never used`() {
        val delegate = CountingDriverFactory()
        val shared = SharedDatabaseDriverFactory(delegate)

        shared.close()

        assertEquals(expected = 0, actual = delegate.createCount)
    }

    companion object {
        private const val CONCURRENT_CALLS = 32
    }
}

private class CloseTrackingDriver(private val delegate: SqlDriver) : SqlDriver by delegate {
    var closed = false
        private set

    override fun close() {
        closed = true
        delegate.close()
    }
}
