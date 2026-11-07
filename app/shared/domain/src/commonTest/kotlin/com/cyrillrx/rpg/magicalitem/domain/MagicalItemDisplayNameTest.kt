package com.cyrillrx.rpg.magicalitem.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class MagicalItemDisplayNameTest {

    @Test
    fun `displayName returns the name of the resolved translation`() {
        val item = itemWithTranslations("en", "fr")

        assertEquals(expected = "en-name", actual = item.displayName("en"))
        assertEquals(expected = "fr-name", actual = item.displayName("fr"))
    }

    @Test
    fun `displayName falls back when the locale is missing`() {
        val item = itemWithTranslations("en")

        assertEquals(expected = "en-name", actual = item.displayName("de"))
    }

    private fun itemWithTranslations(vararg locales: String) = MagicalItem(
        id = "test-item",
        source = "test",
        type = MagicalItem.Type.WEAPON,
        rarity = MagicalItem.Rarity.COMMON,
        attunement = false,
        translations = locales.associateWith { locale ->
            MagicalItem.Translation(
                name = "$locale-name",
                subtype = null,
                description = "$locale-description",
            )
        },
    )
}
