package com.cyrillrx.rpg.core.data.cache

import com.cyrillrx.rpg.core.data.cache.Database.Companion.DATABASE_NAME
import kotlin.io.path.createTempDirectory
import kotlin.io.path.div
import kotlin.io.path.exists
import kotlin.test.Test
import kotlin.test.assertTrue

class DesktopDatabaseDriverFactoryTest {

    @Test
    fun `the database is created in the data directory, which is created when missing`() {
        val dataDirectory = createTempDirectory("ttrpg") / "missing" / "App"

        DesktopDatabaseDriverFactory(dataDirectory).createDriver().close()

        assertTrue((dataDirectory / DATABASE_NAME).exists())
    }
}
