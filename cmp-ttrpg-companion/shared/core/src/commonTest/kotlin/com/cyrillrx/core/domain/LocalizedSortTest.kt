package com.cyrillrx.core.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class LocalizedSortTest {

    @Test
    fun `foldDiacritics strips accents and leaves everything else alone`() {
        assertEquals(expected = "Rodeur", actual = "Rôdeur".foldDiacritics())
        assertEquals(expected = "Ensorceleur", actual = "Ensorceleur".foldDiacritics())
        assertEquals(expected = "eaiou c n", actual = "éàîõù ç ñ".foldDiacritics())
    }

    @Test
    fun `foldDiacritics strips accents from uppercase letters too`() {
        assertEquals(expected = "ECOLE", actual = "ÉCOLE".foldDiacritics())
        assertEquals(expected = "Ecole", actual = "École".foldDiacritics())
    }

    @Test
    fun `foldDiacritics leaves ligatures untouched`() {
        assertEquals(expected = "œuvre", actual = "œuvre".foldDiacritics())
    }

    @Test
    fun `localizedSortKey lowercases before folding`() {
        assertEquals(expected = "rodeur", actual = "Rôdeur".localizedSortKey())
    }

    @Test
    fun `sortedByLocalizedName orders accented names as French does`() {
        val ordered = listOf("Roublard", "Rôdeur").sortedByLocalizedName { it }

        assertEquals(expected = listOf("Rôdeur", "Roublard"), actual = ordered)
    }

    @Test
    fun `sortedByLocalizedName sorts on the localized name, not on the value`() {
        val ordered = listOf("FIGHTER", "BARBARIAN").sortedByLocalizedName { name ->
            when (name) {
                "FIGHTER" -> "Guerrier"
                else -> "Barbare"
            }
        }

        assertEquals(expected = listOf("BARBARIAN", "FIGHTER"), actual = ordered)
    }
}
