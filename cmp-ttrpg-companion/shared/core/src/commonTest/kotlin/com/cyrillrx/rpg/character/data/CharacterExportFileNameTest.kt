package com.cyrillrx.rpg.character.data

import kotlin.test.Test
import kotlin.test.assertEquals

class CharacterExportFileNameTest {
    @Test
    fun `a plain name is kept as is`() {
        assertEquals("Aldwin.character.json", characterExportFileName("Aldwin"))
    }

    @Test
    fun `accents and inner spaces are kept`() {
        assertEquals("Élowen Brume.character.json", characterExportFileName("Élowen Brume"))
    }

    @Test
    fun `characters a platform forbids become spaces`() {
        assertEquals("Ser Aldwin the Bold.character.json", characterExportFileName("Ser/Aldwin:the*Bold"))
        assertEquals("a b c d e f.character.json", characterExportFileName("a\\b?c\"d<e>f"))
        assertEquals("pipe tab.character.json", characterExportFileName("pipe|tab\t"))
    }

    @Test
    fun `whitespace runs collapse into one space`() {
        assertEquals("Aldwin the Bold.character.json", characterExportFileName("Aldwin   the \n Bold"))
    }

    @Test
    fun `leading and trailing spaces and dots are dropped`() {
        assertEquals("Aldwin.character.json", characterExportFileName("  ..Aldwin.. "))
    }

    @Test
    fun `a name left empty falls back to a generic one`() {
        assertEquals("character.character.json", characterExportFileName(""))
        assertEquals("character.character.json", characterExportFileName("   "))
        assertEquals("character.character.json", characterExportFileName("/:*?.."))
    }

    @Test
    fun `a long name is shortened without a trailing space`() {
        val name = "a".repeat(99) + " b" + "c".repeat(50)

        assertEquals("a".repeat(99) + ".character.json", characterExportFileName(name))
    }
}
