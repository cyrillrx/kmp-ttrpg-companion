package com.cyrillrx.rpg.dnd.inventory

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.cyrillrx.rpg.AssetReader
import com.cyrillrx.rpg.api.inventory.ApiInventoryItem
import com.cyrillrx.rpg.api.inventory.ApiMagicalItem
import com.cyrillrx.rpg.common.theme.AppTheme
import com.cyrillrx.utils.deserialize

class InventoryActivity : AppCompatActivity() {

    private val viewModel by viewModels<InventoryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                InventoryScreen(viewModel.magicalItems, viewModel.query, viewModel::applyFilter)
            }
        }

        viewModel.init(loadFromFile())
    }

    private fun loadFromFile(): List<ApiMagicalItem> {
        val serializedInventory = AssetReader.readAsString(this, "objets-magiques.json")
        val inventoryItems: List<ApiInventoryItem> = serializedInventory?.deserialize() ?: listOf()
        return inventoryItems.map { it.toMagicalItem() }
    }

    companion object {
        private fun ApiInventoryItem.toMagicalItem(): ApiMagicalItem {
            return ApiMagicalItem(
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

        private fun ApiInventoryItem.getType(): ApiMagicalItem.Type = when (type) {
            "Armure" -> ApiMagicalItem.Type.ARMOR
            "Potion" -> ApiMagicalItem.Type.POTION
            "Anneau" -> ApiMagicalItem.Type.RING
            "Septre" -> ApiMagicalItem.Type.ROD
            "Parchemin" -> ApiMagicalItem.Type.SCROLL
            "Bâton" -> ApiMagicalItem.Type.STAFF
            "Baguette" -> ApiMagicalItem.Type.WAND
            "Arme" -> ApiMagicalItem.Type.WEAPON
            "Objet merveilleux" -> ApiMagicalItem.Type.WONDROUS_ITEM
            else -> ApiMagicalItem.Type.WONDROUS_ITEM
        }

        private fun ApiInventoryItem.getRarety(): ApiMagicalItem.Rarety = when (rarity) {
            "Courant" -> ApiMagicalItem.Rarety.COMMON
            "Peu courant" -> ApiMagicalItem.Rarety.UNCOMMON
            "Rare" -> ApiMagicalItem.Rarety.RARE
            "Très rare" -> ApiMagicalItem.Rarety.VERY_RARE
            "Légendaire" -> ApiMagicalItem.Rarety.LEGENDARY
            "Artefact" -> ApiMagicalItem.Rarety.ARTIFACT
            else -> ApiMagicalItem.Rarety.UNCOMMON
        }
    }
}
