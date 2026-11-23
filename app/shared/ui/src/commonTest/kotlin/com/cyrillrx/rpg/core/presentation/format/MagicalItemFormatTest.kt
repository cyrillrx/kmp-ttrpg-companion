package com.cyrillrx.rpg.core.presentation.format

import com.cyrillrx.rpg.core.presentation.theme.MagicalItemTypeColors
import com.cyrillrx.rpg.magicalitem.domain.MagicalItem
import kotlin.test.Test
import kotlin.test.assertEquals

class MagicalItemFormatTest {

    @Test
    fun `getColor maps every type to its category color`() {
        assertEquals(expected = MagicalItemTypeColors.armor, actual = itemOf(MagicalItem.Type.ARMOR).getColor())
        assertEquals(expected = MagicalItemTypeColors.other, actual = itemOf(MagicalItem.Type.POTION).getColor())
        assertEquals(expected = MagicalItemTypeColors.other, actual = itemOf(MagicalItem.Type.RING).getColor())
        assertEquals(expected = MagicalItemTypeColors.weapon, actual = itemOf(MagicalItem.Type.ROD).getColor())
        assertEquals(expected = MagicalItemTypeColors.other, actual = itemOf(MagicalItem.Type.SCROLL).getColor())
        assertEquals(expected = MagicalItemTypeColors.weapon, actual = itemOf(MagicalItem.Type.STAFF).getColor())
        assertEquals(expected = MagicalItemTypeColors.weapon, actual = itemOf(MagicalItem.Type.WAND).getColor())
        assertEquals(expected = MagicalItemTypeColors.weapon, actual = itemOf(MagicalItem.Type.WEAPON).getColor())
        assertEquals(expected = MagicalItemTypeColors.other, actual = itemOf(MagicalItem.Type.WONDROUS_ITEM).getColor())
    }

    private fun itemOf(type: MagicalItem.Type) = MagicalItem(
        id = "test-item",
        source = "test",
        type = type,
        rarity = MagicalItem.Rarity.COMMON,
        attunement = false,
        translations = mapOf(
            "en" to MagicalItem.Translation(
                name = "Test Item",
                subtype = null,
                description = "A test item.",
            ),
        ),
    )
}
