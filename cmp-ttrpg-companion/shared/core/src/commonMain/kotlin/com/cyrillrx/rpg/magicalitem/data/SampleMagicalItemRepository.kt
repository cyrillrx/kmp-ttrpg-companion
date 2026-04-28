package com.cyrillrx.rpg.magicalitem.data

import com.cyrillrx.rpg.magicalitem.domain.MagicalItem
import com.cyrillrx.rpg.magicalitem.domain.MagicalItemFilter
import com.cyrillrx.rpg.magicalitem.domain.MagicalItemRepository
import com.cyrillrx.rpg.magicalitem.domain.applyFilter

class SampleMagicalItemRepository : MagicalItemRepository {
    override suspend fun getAll(filter: MagicalItemFilter?): List<MagicalItem> {
        return items.applyFilter(filter)
    }

    override suspend fun getById(id: String): MagicalItem? = items.firstOrNull { it.id == id }

    companion object {
        private val items: List<MagicalItem> = listOf(
            oathAxe(),
            healingPotion(),
            cloakOfProtection(),
            shieldPlus1(),
            wandOfFireballs(),
            ringOfProtection(),
            fireballScroll(),
            staffOfPower(),
        )

        fun getAll(): List<MagicalItem> = items

        fun getFirst(): MagicalItem = items.first()

        fun oathAxe() = MagicalItem(
            id = "oath-axe",
            source = "srd_5.1",
            type = MagicalItem.Type.WEAPON,
            rarity = MagicalItem.Rarity.RARE,
            attunement = true,
            translations = mapOf(
                "en" to MagicalItem.Translation(
                    name = "Oath Axe",
                    subtype = null,
                    description = "When you make a melee attack with this weapon, you can speak its command word. The target of your attack becomes your sworn enemy.",
                ),
            ),
        )

        fun healingPotion() = MagicalItem(
            id = "healing-potion",
            source = "srd_5.1",
            type = MagicalItem.Type.POTION,
            rarity = MagicalItem.Rarity.COMMON,
            attunement = false,
            translations = mapOf(
                "en" to MagicalItem.Translation(
                    name = "Healing Potion",
                    subtype = null,
                    description = "You regain 2d4 + 2 hit points when you drink this potion.",
                ),
                "fr" to MagicalItem.Translation(
                    name = "Potion de soin",
                    subtype = null,
                    description = "Vous récupérez 2d4 + 2 points de vie lorsque vous buvez cette potion.",
                ),
            ),
        )

        private fun cloakOfProtection() = MagicalItem(
            id = "cloak-of-protection",
            source = "srd_5.1",
            type = MagicalItem.Type.WONDROUS_ITEM,
            rarity = MagicalItem.Rarity.UNCOMMON,
            attunement = true,
            translations = mapOf(
                "en" to MagicalItem.Translation(
                    name = "Cloak of Protection",
                    subtype = null,
                    description = "You gain a +1 bonus to AC and saving throws while you wear this cloak.",
                ),
            ),
        )

        private fun shieldPlus1() = MagicalItem(
            id = "shield-plus-1",
            source = "srd_5.1",
            type = MagicalItem.Type.ARMOR,
            rarity = MagicalItem.Rarity.UNCOMMON,
            attunement = false,
            translations = mapOf(
                "en" to MagicalItem.Translation(
                    name = "Shield +1",
                    subtype = "shield",
                    description = "While holding this shield, you have a +1 bonus to AC in addition to the shield's normal bonus.",
                ),
            ),
        )

        private fun wandOfFireballs() = MagicalItem(
            id = "wand-of-fireballs",
            source = "srd_5.1",
            type = MagicalItem.Type.WAND,
            rarity = MagicalItem.Rarity.RARE,
            attunement = true,
            translations = mapOf(
                "en" to MagicalItem.Translation(
                    name = "Wand of Fireballs",
                    subtype = null,
                    description = "This wand has 7 charges. While holding it, you can use an action to expend 1 or more of its charges to cast the fireball spell.",
                ),
            ),
        )

        private fun ringOfProtection() = MagicalItem(
            id = "ring-of-protection",
            source = "srd_5.1",
            type = MagicalItem.Type.RING,
            rarity = MagicalItem.Rarity.RARE,
            attunement = true,
            translations = mapOf(
                "en" to MagicalItem.Translation(
                    name = "Ring of Protection",
                    subtype = null,
                    description = "You gain a +1 bonus to AC and saving throws while wearing this ring.",
                ),
            ),
        )

        private fun fireballScroll() = MagicalItem(
            id = "fireball-scroll",
            source = "srd_5.1",
            type = MagicalItem.Type.SCROLL,
            rarity = MagicalItem.Rarity.UNCOMMON,
            attunement = false,
            translations = mapOf(
                "en" to MagicalItem.Translation(
                    name = "Fireball Scroll",
                    subtype = null,
                    description = "A fireball spell is written on this scroll. If the spell is on your class's spell list, you can cast it.",
                ),
            ),
        )

        private fun staffOfPower() = MagicalItem(
            id = "staff-of-power",
            source = "srd_5.1",
            type = MagicalItem.Type.STAFF,
            rarity = MagicalItem.Rarity.VERY_RARE,
            attunement = true,
            translations = mapOf(
                "en" to MagicalItem.Translation(
                    name = "Staff of Power",
                    subtype = null,
                    description = "This staff grants a +2 bonus to attack and damage rolls made with it as a melee weapon.",
                ),
            ),
        )
    }
}
