package com.cyrillrx.rpg.core.data

import kotlin.io.path.Path
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationDataDirectoryTest {

    @Test
    fun `macOS keeps the data in Application Support`() {
        assertEquals(
            expected = Path("/Users/me/Library/Application Support/App"),
            actual = applicationDataDirectory(
                appName = "App",
                osName = "Mac OS X",
                home = HOME,
                environment = { null },
            ),
        )
    }

    @Test
    fun `Windows keeps the data in the local app data folder`() {
        val environment = mapOf("LOCALAPPDATA" to "/local")

        assertEquals(
            expected = Path("/local/App"),
            actual = applicationDataDirectory(
                appName = "App",
                osName = "Windows 11",
                home = HOME,
                environment = environment::get,
            ),
        )
    }

    @Test
    fun `Windows without LOCALAPPDATA falls back to its conventional location`() {
        assertEquals(
            expected = Path("/Users/me/AppData/Local/App"),
            actual = applicationDataDirectory(
                appName = "App",
                osName = "Windows 11",
                home = HOME,
                environment = { null },
            ),
        )
    }

    @Test
    fun `Linux keeps the data in XDG_DATA_HOME`() {
        val environment = mapOf("XDG_DATA_HOME" to "/xdg")

        assertEquals(
            expected = Path("/xdg/App"),
            actual = applicationDataDirectory(
                appName = "App",
                osName = "Linux",
                home = HOME,
                environment = environment::get,
            ),
        )
    }

    @Test
    fun `a relative XDG_DATA_HOME is ignored as the XDG specification requires`() {
        val environment = mapOf("XDG_DATA_HOME" to "data")

        assertEquals(
            expected = Path("/Users/me/.local/share/App"),
            actual = applicationDataDirectory(
                appName = "App",
                osName = "Linux",
                home = HOME,
                environment = environment::get,
            ),
        )
    }

    private companion object {
        val HOME = Path("/Users/me")
    }
}
