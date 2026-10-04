package com.cyrillrx.rpg.character.data

import com.cyrillrx.core.data.Imported
import com.cyrillrx.core.domain.Result
import kotlinx.datetime.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Instant

class CharacterExportReaderTest {
    private val exported = SampleCharacterRepository.humanFighter()
        .toExportFile(APP_VERSION, Instant.parse("2026-09-19T10:24:00Z"), TimeZone.UTC)
        .content

    @Test
    fun `an exported file reads back as the exported sheet`() {
        val result = readCharacterExport(exported)

        assertEquals(Result.Success(Imported(SampleCharacterRepository.humanFighter(), emptyList())), result)
    }

    @Test
    fun `an out-of-range value is clamped and reported`() {
        val result = readCharacterExport(exported.replace(""""armorClass": 16""", """"armorClass": 5000"""))

        val imported = (result as Result.Success).value
        assertEquals(40, imported.value.armorClass)
        assertEquals(
            listOf(CharacterImportWarning.ValueCoerced(SAMPLE_ID, "armor class", declared = "5000", kept = "40")),
            imported.warnings,
        )
    }

    @Test
    fun `a file that is not JSON is rejected`() {
        assertEquals(Result.Failure(CharacterFileImportError.InvalidJson), readCharacterExport("not a character"))
    }

    @Test
    fun `a JSON value that is not an object is rejected as a malformed envelope`() {
        assertEquals(Result.Failure(CharacterFileImportError.MalformedEnvelope), readCharacterExport("[1, 2]"))
    }

    @Test
    fun `a file without a format version is rejected as a malformed envelope`() {
        val result = readCharacterExport(exported.withoutLine(""""formatVersion""""))

        assertEquals(Result.Failure(CharacterFileImportError.MalformedEnvelope), result)
    }

    @Test
    fun `a non-positive format version is rejected as a malformed envelope`() {
        val result = readCharacterExport(exported.replace(""""formatVersion": 1""", """"formatVersion": 0"""))

        assertEquals(Result.Failure(CharacterFileImportError.MalformedEnvelope), result)
    }

    @Test
    fun `a file without an entity type is rejected as a malformed envelope`() {
        val result = readCharacterExport(exported.withoutLine(""""entityType""""))

        assertEquals(Result.Failure(CharacterFileImportError.MalformedEnvelope), result)
    }

    @Test
    fun `a file without a character is rejected as a malformed envelope`() {
        val result = readCharacterExport("""{"formatVersion": 1, "entityType": "character"}""")

        assertEquals(Result.Failure(CharacterFileImportError.MalformedEnvelope), result)
    }

    @Test
    fun `a wrongly typed character field is rejected as a malformed envelope`() {
        val result = readCharacterExport(exported.replace(""""armorClass": 16""", """"armorClass": [16]"""))

        assertEquals(Result.Failure(CharacterFileImportError.MalformedEnvelope), result)
    }

    @Test
    fun `a file from a newer format version is refused`() {
        val result = readCharacterExport(exported.replace(""""formatVersion": 1""", """"formatVersion": 2"""))

        assertEquals(Result.Failure(CharacterFileImportError.UnsupportedFormatVersion(2)), result)
    }

    @Test
    fun `a newer format version is refused even when its character no longer decodes`() {
        val result = readCharacterExport(
            exported
                .replace(""""formatVersion": 1""", """"formatVersion": 2""")
                .replace(""""armorClass": 16""", """"armorClass": [16]"""),
        )

        assertEquals(Result.Failure(CharacterFileImportError.UnsupportedFormatVersion(2)), result)
    }

    @Test
    fun `a file holding another entity type is refused`() {
        val result = readCharacterExport(exported.replace(""""entityType": "character"""", """"entityType": "spell""""))

        assertEquals(Result.Failure(CharacterFileImportError.UnsupportedEntityType("spell")), result)
    }

    @Test
    fun `an invalid character is refused with the reason the sheet gave`() {
        val result = readCharacterExport(exported.replace(""""race": "human"""", """"race": "kobold""""))

        assertEquals(
            Result.Failure(
                CharacterFileImportError.InvalidCharacter(CharacterImportError.UnknownRace(SAMPLE_ID, "kobold")),
            ),
            result,
        )
    }

    private fun String.withoutLine(key: String): String = lines().filterNot { key in it }.joinToString("\n")

    private companion object {
        const val APP_VERSION = "1.4.0"
        val SAMPLE_ID: String = SampleCharacterRepository.humanFighter().id
    }
}
