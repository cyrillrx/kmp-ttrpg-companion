package com.cyrillrx.rpg.magicalitems.data

import com.cyrillrx.core.data.FileReader
import com.cyrillrx.core.data.deserialize
import com.cyrillrx.core.domain.Result
import com.cyrillrx.rpg.magicalitems.data.api.ApiInventoryItem
import com.cyrillrx.rpg.magicalitems.domain.MagicalItem
import com.cyrillrx.rpg.magicalitems.domain.MagicalItemRepository

class JsonMagicalItemRepository(private val fileReader: FileReader) : MagicalItemRepository {

    override suspend fun getAll(): List<MagicalItem> {
        val inventoryItems = loadFromFile()
        return inventoryItems.map { it.toMagicalItem() }
    }

    override suspend fun filter(query: String): List<MagicalItem> {
        return getAll().filter(query)
    }

    private suspend fun loadFromFile(): List<ApiInventoryItem> {
        val result = fileReader.readFile("files/objets-magiques.json")
        if (result is Result.Success) {
            return result.value.deserialize() ?: listOf()
        }
        return listOf()
    }

    companion object {
        private fun List<MagicalItem>.filter(query: String): ArrayList<MagicalItem> =
            filterTo(ArrayList()) { spell -> spell.filter(query) }

        private fun MagicalItem.filter(query: String): Boolean {
            val lowerCaseQuery = query.trim().lowercase()
            return title.lowercase().contains(lowerCaseQuery) ||
                subtitle.lowercase().contains(lowerCaseQuery) ||
                description.lowercase().contains(lowerCaseQuery)
        }

        private fun ApiInventoryItem.toMagicalItem(): MagicalItem {
            return MagicalItem(
                title = title,
                subtitle = getSubtitle(),
                description = content,
                type = getType(),
                rarety = getRarety(),
                attunement = attunement != null,
            )
        }

        private fun ApiInventoryItem.getSubtitle(): String {
            val attunementString = attunement?.let { " ($attunement)" } ?: ""
            return "$type, $rarity$attunementString"
        }

        private fun ApiInventoryItem.getType(): MagicalItem.Type = when (type) {
            "Armure" -> MagicalItem.Type.ARMOR
            "Potion" -> MagicalItem.Type.POTION
            "Anneau" -> MagicalItem.Type.RING
            "Septre" -> MagicalItem.Type.ROD
            "Parchemin" -> MagicalItem.Type.SCROLL
            "Bâton" -> MagicalItem.Type.STAFF
            "Baguette" -> MagicalItem.Type.WAND
            "Arme" -> MagicalItem.Type.WEAPON
            "Objet merveilleux" -> MagicalItem.Type.WONDROUS_ITEM
            else -> MagicalItem.Type.WONDROUS_ITEM
        }

        private fun ApiInventoryItem.getRarety(): MagicalItem.Rarety = when (rarity) {
            "Courant" -> MagicalItem.Rarety.COMMON
            "Peu courant" -> MagicalItem.Rarety.UNCOMMON
            "Rare" -> MagicalItem.Rarety.RARE
            "Très rare" -> MagicalItem.Rarety.VERY_RARE
            "Légendaire" -> MagicalItem.Rarety.LEGENDARY
            "Artefact" -> MagicalItem.Rarety.ARTIFACT
            else -> MagicalItem.Rarety.UNCOMMON
        }
    }
}
