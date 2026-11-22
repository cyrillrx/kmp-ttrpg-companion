package com.cyrillrx.rpg.character.data

import com.cyrillrx.core.domain.ExportFile
import com.cyrillrx.core.domain.Imported
import com.cyrillrx.core.domain.Result
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.CharacterFileFormat
import com.cyrillrx.rpg.character.domain.CharacterFileImportError
import com.cyrillrx.rpg.character.domain.CharacterImportWarning
import kotlinx.datetime.TimeZone
import kotlin.time.Instant

object JsonCharacterFileFormat : CharacterFileFormat {
    override fun read(content: String): Result<Imported<Character, CharacterImportWarning>, CharacterFileImportError> =
        readCharacterExport(content)

    override fun write(character: Character, appVersion: String, exportedAt: Instant, timeZone: TimeZone): ExportFile =
        character.toExportFile(appVersion, exportedAt, timeZone)
}
