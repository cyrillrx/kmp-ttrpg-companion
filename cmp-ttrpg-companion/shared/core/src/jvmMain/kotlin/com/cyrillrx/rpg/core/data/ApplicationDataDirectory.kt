package com.cyrillrx.rpg.core.data

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.div

/** Per-user directory the desktop app persists everything in, at the location each OS reserves for it. */
fun applicationDataDirectory(
    appName: String,
    osName: String = System.getProperty("os.name"),
    home: Path = Path(System.getProperty("user.home")),
    environment: (String) -> String? = System::getenv,
): Path {
    val lowercaseOsName = osName.lowercase()
    return when {
        lowercaseOsName.startsWith("mac") -> home / "Library" / "Application Support" / appName
        lowercaseOsName.startsWith("windows") -> {
            val localAppData = environment.readAbsolutePath(variable = "LOCALAPPDATA")
            (localAppData ?: home) / appName / "data"
        }
        else -> (environment.readAbsolutePath(variable = "XDG_DATA_HOME") ?: home / ".local" / "share") / appName
    }
}

private fun ((String) -> String?).readAbsolutePath(variable: String): Path? =
    invoke(variable)?.let(::Path)?.takeIf(Path::isAbsolute)
