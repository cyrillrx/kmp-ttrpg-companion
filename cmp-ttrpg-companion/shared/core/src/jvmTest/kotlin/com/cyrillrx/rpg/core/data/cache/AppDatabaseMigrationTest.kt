package com.cyrillrx.rpg.core.data.cache

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.cyrillrx.rpg.cache.AppDatabase
import kotlin.test.Test
import kotlin.test.assertEquals

class AppDatabaseMigrationTest {

    @Test
    fun `migrating from v1 adds the palette column and preserves existing preferences`() {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)

        // Recreate the v1 shape of the migrated table (no palette column) and seed a non-default row.
        driver.execute(
            null,
            """
            CREATE TABLE UserPreferencesEntity (
                id INTEGER NOT NULL PRIMARY KEY DEFAULT 1,
                theme TEXT NOT NULL DEFAULT 'system',
                distance_unit TEXT NOT NULL DEFAULT 'feet'
            );
            """.trimIndent(),
            0,
        )
        driver.execute(
            null,
            "INSERT INTO UserPreferencesEntity (id, theme, distance_unit) VALUES (1, 'dark', 'meters');",
            0,
        )

        AppDatabase.Schema.migrate(driver, oldVersion = 1L, newVersion = AppDatabase.Schema.version)

        val preferences = AppDatabase(driver).appDatabaseQueries
            .getUserPreferences { _, theme, palette, distanceUnit -> Triple(theme, palette, distanceUnit) }
            .executeAsOne()

        assertEquals("arcane", preferences.second) // new column defaults to arcane
        assertEquals("dark", preferences.first) // pre-existing values are untouched
        assertEquals("meters", preferences.third)
    }
}
