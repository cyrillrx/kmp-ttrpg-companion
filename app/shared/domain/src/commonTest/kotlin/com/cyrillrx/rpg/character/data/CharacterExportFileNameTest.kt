package com.cyrillrx.rpg.character.data

import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

private const val MAX_NAME_LENGTH = 74
private const val SUFFIX = "_lvl20_2026-10-02.character.json"

class CharacterExportFileNameTest {
    @Test
    fun `the name is followed by the level and the date`() {
        assertEquals(
            "aldwin_lvl3_2026-10-02.character.json",
            characterExportFileName("Aldwin", 3, LocalDate(2026, 10, 2)),
        )
    }

    @Test
    fun `the name is lowercased`() {
        assertEquals("aldwin$SUFFIX", fileName("ALDWIN"))
    }

    @Test
    fun `accents are kept and spaces become hyphens`() {
        assertEquals("élowen-brume$SUFFIX", fileName("Élowen Brume"))
    }

    @Test
    fun `characters a platform forbids become hyphens`() {
        assertEquals("ser-aldwin-the-bold$SUFFIX", fileName("Ser/Aldwin:the*Bold"))
        assertEquals("a-b-c-d-e-f$SUFFIX", fileName("a\\b?c\"d<e>f"))
        assertEquals("pipe-tab$SUFFIX", fileName("pipe|tab\t"))
    }

    @Test
    fun `dots and underscores become hyphens`() {
        assertEquals("st-aldwin-the-bold$SUFFIX", fileName("St.Aldwin_the_Bold"))
    }

    @Test
    fun `the only dot is the one starting the suffix`() {
        val fileName = fileName("Con.Nul")

        assertEquals("con-nul$SUFFIX", fileName)
        assertFalse('.' in fileName.removeSuffix(".character.json"))
    }

    @Test
    fun `separator runs collapse into one hyphen`() {
        assertEquals("aldwin-the-bold$SUFFIX", fileName("Aldwin  - the \n _Bold"))
    }

    @Test
    fun `leading and trailing separators are dropped`() {
        assertEquals("aldwin$SUFFIX", fileName("  ..Aldwin-- "))
    }

    @Test
    fun `a name left empty falls back to a generic one`() {
        assertEquals("character$SUFFIX", fileName(""))
        assertEquals("character$SUFFIX", fileName("   "))
        assertEquals("character$SUFFIX", fileName("/:*?.._-"))
    }

    @Test
    fun `a long name is shortened without a trailing hyphen`() {
        val name = "a".repeat(MAX_NAME_LENGTH - 1) + " b" + "c".repeat(50)

        assertEquals("a".repeat(MAX_NAME_LENGTH - 1) + SUFFIX, fileName(name))
    }

    @Test
    fun `a long name in a multibyte script fits the 255-byte limit`() {
        val fileName = fileName("龍".repeat(100))

        assertEquals("龍".repeat(MAX_NAME_LENGTH) + SUFFIX, fileName)
        assertTrue(fileName.encodeToByteArray().size <= 255)
    }

    @Test
    fun `an emoji straddling the limit is dropped whole`() {
        val name = "a".repeat(MAX_NAME_LENGTH - 1) + "🐉"

        assertEquals("a".repeat(MAX_NAME_LENGTH - 1) + SUFFIX, fileName(name))
    }

    private fun fileName(characterName: String): String =
        characterExportFileName(characterName, 20, LocalDate(2026, 10, 2))
}
