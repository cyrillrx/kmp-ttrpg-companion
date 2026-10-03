package com.cyrillrx.rpg.character.data

import com.cyrillrx.core.data.ExportFile
import com.cyrillrx.core.data.exportSerializer
import com.cyrillrx.rpg.character.data.api.ApiCharacterExport
import com.cyrillrx.rpg.character.data.api.CHARACTER_EXPORT_FORMAT_VERSION
import com.cyrillrx.rpg.character.data.api.ENTITY_TYPE_CHARACTER
import com.cyrillrx.rpg.character.domain.Character
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

private const val JSON_MIME_TYPE = "application/json"
private const val CHARACTER_FILE_SUFFIX = ".character.json"
private const val FALLBACK_FILE_NAME = "character"
private const val SEGMENT_SEPARATOR = '_'
private const val WORD_SEPARATOR = '-'

// APFS and ext4
private const val MAX_FILE_NAME_BYTES = 255

// e.g. "_lvl20_2026-10-02.character.json"
private const val MAX_SUFFIX_BYTES = 32
private const val MAX_UTF8_BYTES_PER_UTF16_UNIT = 3
private const val MAX_NAME_LENGTH = (MAX_FILE_NAME_BYTES - MAX_SUFFIX_BYTES) / MAX_UTF8_BYTES_PER_UTF16_UNIT

// Forbidden on at least one of the platforms a file may travel to, plus the dot and the underscore:
// keeping the first dot for the suffix leaves no Windows device name (CON, NUL…) in front of it.
private val wordBreak = Regex("""[\s/\\:*?"<>|._\-\u0000-\u001F\u007F]+""")

fun Character.toExportFile(appVersion: String, exportedAt: Instant, timeZone: TimeZone): ExportFile = ExportFile(
    name = characterExportFileName(name, totalLevel, exportedAt.toLocalDateTime(timeZone).date),
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

// e.g. "aldwin-le-brave_lvl10_2026-10-02.character.json"
fun characterExportFileName(characterName: String, totalLevel: Int, exportDate: LocalDate): String {
    val baseName = characterName
        .lowercase()
        .replace(wordBreak, WORD_SEPARATOR.toString())
        .trim(WORD_SEPARATOR)
        .take(MAX_NAME_LENGTH)
        .dropLoneHighSurrogate()
        .trimEnd(WORD_SEPARATOR)
        .ifEmpty { FALLBACK_FILE_NAME }
    return "$baseName${SEGMENT_SEPARATOR}lvl$totalLevel$SEGMENT_SEPARATOR$exportDate$CHARACTER_FILE_SUFFIX"
}

private fun String.dropLoneHighSurrogate(): String = if (lastOrNull()?.isHighSurrogate() == true) dropLast(1) else this
