package com.cyrillrx.rpg.core.presentation.format

import com.cyrillrx.rpg.core.presentation.theme.ArmorColor
import com.cyrillrx.rpg.core.presentation.theme.ObjectColor
import com.cyrillrx.rpg.core.presentation.theme.WeaponColor
import com.cyrillrx.rpg.magicalitem.domain.MagicalItem
import kotlin.test.Test
import kotlin.test.assertEquals

class MagicalItemFormatExtTest {

    @Test
    fun `getColor maps every type to its category color`() {
        assertEquals(expected = ArmorColor, actual = itemOf(MagicalItem.Type.ARMOR).getColor())
        assertEquals(expected = ObjectColor, actual = itemOf(MagicalItem.Type.POTION).getColor())
        assertEquals(expected = ObjectColor, actual = itemOf(MagicalItem.Type.RING).getColor())
        assertEquals(expected = WeaponColor, actual = itemOf(MagicalItem.Type.ROD).getColor())
        assertEquals(expected = ObjectColor, actual = itemOf(MagicalItem.Type.SCROLL).getColor())
        assertEquals(expected = WeaponColor, actual = itemOf(MagicalItem.Type.STAFF).getColor())
        assertEquals(expected = WeaponColor, actual = itemOf(MagicalItem.Type.WAND).getColor())
        assertEquals(expected = WeaponColor, actual = itemOf(MagicalItem.Type.WEAPON).getColor())
        assertEquals(expected = ObjectColor, actual = itemOf(MagicalItem.Type.WONDROUS_ITEM).getColor())
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
