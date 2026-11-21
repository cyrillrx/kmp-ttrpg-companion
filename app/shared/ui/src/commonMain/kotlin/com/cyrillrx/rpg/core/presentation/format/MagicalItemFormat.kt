package com.cyrillrx.rpg.core.presentation.format

import androidx.compose.ui.graphics.Color
import com.cyrillrx.rpg.core.presentation.theme.MagicalItemTypeColors
import com.cyrillrx.rpg.magicalitem.domain.MagicalItem

fun MagicalItem.getColor(): Color = when (type) {
    MagicalItem.Type.ARMOR -> MagicalItemTypeColors.armor
    MagicalItem.Type.POTION -> MagicalItemTypeColors.other
    MagicalItem.Type.RING -> MagicalItemTypeColors.other
    MagicalItem.Type.ROD -> MagicalItemTypeColors.weapon
    MagicalItem.Type.SCROLL -> MagicalItemTypeColors.other
    MagicalItem.Type.STAFF -> MagicalItemTypeColors.weapon
    MagicalItem.Type.WAND -> MagicalItemTypeColors.weapon
    MagicalItem.Type.WEAPON -> MagicalItemTypeColors.weapon
    MagicalItem.Type.WONDROUS_ITEM -> MagicalItemTypeColors.other
}
