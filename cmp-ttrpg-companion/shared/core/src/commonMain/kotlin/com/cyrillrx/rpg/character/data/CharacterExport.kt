package com.cyrillrx.rpg.character.data

import com.cyrillrx.core.data.ExportFile
import com.cyrillrx.core.data.exportSerializer
import com.cyrillrx.rpg.character.data.api.ApiCharacterExport
import com.cyrillrx.rpg.character.data.api.CHARACTER_EXPORT_FORMAT_VERSION
import com.cyrillrx.rpg.character.data.api.ENTITY_TYPE_CHARACTER
import com.cyrillrx.rpg.character.domain.Character
import kotlin.time.Instant

private const val JSON_MIME_TYPE = "application/json"
private const val CHARACTER_FILE_SUFFIX = ".character.json"
private const val FALLBACK_FILE_NAME = "character"

// A UTF-16 unit takes at most 3 UTF-8 bytes, so 80 units plus the suffix stay within the
// 255-byte name limit of APFS and ext4.
private const val MAX_FILE_NAME_LENGTH = 80

// Forbidden on at least one of the platforms a file may travel to, Windows being the strictest.
private val forbiddenFileNameChars = Regex("""[/\\:*?"<>|\u0000-\u001F\u007F]""")
private val whitespaceRun = Regex("""\s+""")

fun Character.toExportFile(appVersion: String, exportedAt: Instant): ExportFile = ExportFile(
    name = characterExportFileName(name),
    mimeType = JSON_MIME_TYPE,
    content = exportSerializer.encodeToString(
        ApiCharacterExport(
            formatVersion = CHARACTER_EXPORT_FORMAT_VERSION,
            entityType = ENTITY_TYPE_CHARACTER,
            appVersion = appVersion,
            exportedAt = exportedAt.toString(),
            sourceId = id,
            character = toApiCharacter(),
        ),
    ),
)

// e.g. "Aldwin.character.json"
fun characterExportFileName(characterName: String): String {
    val baseName = characterName
        .replace(forbiddenFileNameChars, " ")
        .replace(whitespaceRun, " ")
        .trim(' ', '.')
        .take(MAX_FILE_NAME_LENGTH)
        .dropLoneHighSurrogate()
        .trimEnd(' ', '.')
        .ifEmpty { FALLBACK_FILE_NAME }
    return baseName + CHARACTER_FILE_SUFFIX
}

private fun String.dropLoneHighSurrogate(): String = if (lastOrNull()?.isHighSurrogate() == true) dropLast(1) else this
