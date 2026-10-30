package com.cyrillrx.rpg.core.data.cache

import com.cyrillrx.rpg.core.data.cache.Database.Companion.DATABASE_NAME
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.createTempDirectory
import kotlin.io.path.deleteRecursively
import kotlin.io.path.div
import kotlin.io.path.exists
import kotlin.test.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalPathApi::class)
class DesktopDatabaseDriverFactoryTest {

    @Test
    fun `the database is created in the data directory even when it is missing`() {
        val root = createTempDirectory("ttrpg")
        val dataDirectory = root / "missing" / "App"

        try {
            DesktopDatabaseDriverFactory(dataDirectory).createDriver().close()

            assertTrue((dataDirectory / DATABASE_NAME).exists())
        } finally {
            root.deleteRecursively()
        }
    }
}
