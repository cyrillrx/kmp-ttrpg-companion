package com.cyrillrx.rpg.character.domain

import com.cyrillrx.core.domain.ExportFile
import com.cyrillrx.core.domain.Imported
import com.cyrillrx.core.domain.Result
import kotlinx.datetime.TimeZone
import kotlin.time.Instant

interface CharacterFileFormat {
    fun read(content: String): Result<Imported<Character, CharacterImportWarning>, CharacterFileImportError>

    fun write(character: Character, appVersion: String, exportedAt: Instant, timeZone: TimeZone): ExportFile
}
