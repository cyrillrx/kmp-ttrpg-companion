package com.cyrillrx.rpg.core.presentation.component.dnd

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.cyrillrx.rpg.magicalitem.domain.MagicalItem
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.item_rarity_artifact
import rpg_companion.composeapp.generated.resources.item_rarity_common
import rpg_companion.composeapp.generated.resources.item_rarity_legendary
import rpg_companion.composeapp.generated.resources.item_rarity_rare
import rpg_companion.composeapp.generated.resources.item_rarity_uncommon
import rpg_companion.composeapp.generated.resources.item_rarity_very_rare
import rpg_companion.composeapp.generated.resources.item_type_armor
import rpg_companion.composeapp.generated.resources.item_type_potion
import rpg_companion.composeapp.generated.resources.item_type_ring
import rpg_companion.composeapp.generated.resources.item_type_rod
import rpg_companion.composeapp.generated.resources.item_type_scroll
import rpg_companion.composeapp.generated.resources.item_type_staff
import rpg_companion.composeapp.generated.resources.item_type_wand
import rpg_companion.composeapp.generated.resources.item_type_weapon
import rpg_companion.composeapp.generated.resources.item_type_wondrous_item

@Composable
fun MagicalItem.Type.toFormattedString(): String {
    val stringRes = when (this) {
        MagicalItem.Type.ARMOR -> Res.string.item_type_armor
        MagicalItem.Type.POTION -> Res.string.item_type_potion
        MagicalItem.Type.RING -> Res.string.item_type_ring
        MagicalItem.Type.ROD -> Res.string.item_type_rod
        MagicalItem.Type.SCROLL -> Res.string.item_type_scroll
        MagicalItem.Type.STAFF -> Res.string.item_type_staff
        MagicalItem.Type.WAND -> Res.string.item_type_wand
        MagicalItem.Type.WEAPON -> Res.string.item_type_weapon
        MagicalItem.Type.WONDROUS_ITEM -> Res.string.item_type_wondrous_item
    }
    return stringResource(stringRes)
}

@Composable
fun MagicalItem.Rarity.toFormattedString(): String {
    val stringRes = when (this) {
        MagicalItem.Rarity.COMMON -> Res.string.item_rarity_common
        MagicalItem.Rarity.UNCOMMON -> Res.string.item_rarity_uncommon
        MagicalItem.Rarity.RARE -> Res.string.item_rarity_rare
        MagicalItem.Rarity.VERY_RARE -> Res.string.item_rarity_very_rare
        MagicalItem.Rarity.LEGENDARY -> Res.string.item_rarity_legendary
        MagicalItem.Rarity.ARTIFACT -> Res.string.item_rarity_artifact
    }
    return stringResource(stringRes)
}

private val weaponColor = Color(155, 11, 78)
private val armorColor = Color(0, 122, 179)
private val objectColor = Color(0, 179, 140)

fun MagicalItem.getColor(): Color = when (type) {
    MagicalItem.Type.ARMOR -> armorColor
    MagicalItem.Type.POTION -> objectColor
    MagicalItem.Type.RING -> objectColor
    MagicalItem.Type.ROD -> weaponColor
    MagicalItem.Type.SCROLL -> objectColor
    MagicalItem.Type.STAFF -> weaponColor
    MagicalItem.Type.WAND -> weaponColor
    MagicalItem.Type.WEAPON -> weaponColor
    MagicalItem.Type.WONDROUS_ITEM -> objectColor
}
