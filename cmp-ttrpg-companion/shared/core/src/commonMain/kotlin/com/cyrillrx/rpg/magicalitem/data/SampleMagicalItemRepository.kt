package com.cyrillrx.rpg.magicalitem.data

import com.cyrillrx.rpg.magicalitem.domain.MagicalItem
import com.cyrillrx.rpg.magicalitem.domain.MagicalItemFilter
import com.cyrillrx.rpg.magicalitem.domain.MagicalItemRepository

class SampleMagicalItemRepository : MagicalItemRepository {
    override suspend fun getAll(filter: MagicalItemFilter?): List<MagicalItem> {
        filter ?: return items
        return items.filter(filter::matches)
    }

    override suspend fun getById(id: String): MagicalItem? = items.firstOrNull { it.id == id }

    companion object {
        private val items: List<MagicalItem> = listOf(
            oathAxe(),
            cloakOfProtection(),
            healingPotion(),
            shieldPlus1(),
            wandOfFireballs(),
            ringOfProtection(),
            fireballScroll(),
            staffOfPower(),
        )

        fun getAll(): List<MagicalItem> = items

        fun getFirst(): MagicalItem = items.first()

        private fun oathAxe() = MagicalItem(
            id = "Oath Axe",
            title = "Oath Axe",
            subtitle = "Weapon (battleaxe), unique (requires attunement)",
            description = "When you make a melee attack with this weapon, you can speak its command word. The target of your attack becomes your sworn enemy.",
            type = MagicalItem.Type.WEAPON,
            rarity = MagicalItem.Rarity.RARE,
            attunement = true,
        )

        private fun cloakOfProtection() = MagicalItem(
            id = "Cloak of Protection",
            title = "Cloak of Protection",
            subtitle = "Wondrous item, uncommon (requires attunement)",
            description = "You gain a +1 bonus to AC and saving throws while you wear this cloak.",
            type = MagicalItem.Type.WONDROUS_ITEM,
            rarity = MagicalItem.Rarity.UNCOMMON,
            attunement = true,
        )

        private fun healingPotion() = MagicalItem(
            id = "Healing Potion",
            title = "Healing Potion",
            subtitle = "Potion, common",
            description = "You regain 2d4 + 2 hit points when you drink this potion.",
            type = MagicalItem.Type.POTION,
            rarity = MagicalItem.Rarity.COMMON,
            attunement = false,
        )

        private fun shieldPlus1() = MagicalItem(
            id = "Shield +1",
            title = "Shield +1",
            subtitle = "Armor (shield), uncommon",
            description = "While holding this shield, you have a +1 bonus to AC in addition to the shield's normal bonus.",
            type = MagicalItem.Type.ARMOR,
            rarity = MagicalItem.Rarity.UNCOMMON,
            attunement = false,
        )

        private fun wandOfFireballs() = MagicalItem(
            id = "Wand of Fireballs",
            title = "Wand of Fireballs",
            subtitle = "Wand, rare (requires attunement by a spellcaster)",
            description = "This wand has 7 charges. While holding it, you can use an action to expend 1 or more of its charges to cast the fireball spell.",
            type = MagicalItem.Type.WAND,
            rarity = MagicalItem.Rarity.RARE,
            attunement = true,
        )

        private fun ringOfProtection() = MagicalItem(
            id = "Ring of Protection",
            title = "Ring of Protection",
            subtitle = "Ring, rare (requires attunement)",
            description = "You gain a +1 bonus to AC and saving throws while wearing this ring.",
            type = MagicalItem.Type.RING,
            rarity = MagicalItem.Rarity.RARE,
            attunement = true,
        )

        private fun fireballScroll() = MagicalItem(
            id = "Fireball Scroll",
            title = "Fireball Scroll",
            subtitle = "Scroll, uncommon",
            description = "A fireball spell is written on this scroll. If the spell is on your class's spell list, you can cast it.",
            type = MagicalItem.Type.SCROLL,
            rarity = MagicalItem.Rarity.UNCOMMON,
            attunement = false,
        )

        private fun staffOfPower() = MagicalItem(
            id = "Staff of Power",
            title = "Staff of Power",
            subtitle = "Staff, very rare (requires attunement by a spellcaster)",
            description = "This staff grants a +2 bonus to attack and damage rolls made with it as a melee weapon.",
            type = MagicalItem.Type.STAFF,
            rarity = MagicalItem.Rarity.VERY_RARE,
            attunement = true,
        )
    }
}
