package com.cyrillrx.rpg.core.presentation.format

import androidx.compose.ui.graphics.Color
import com.cyrillrx.rpg.core.presentation.theme.ArmorColor
import com.cyrillrx.rpg.core.presentation.theme.ObjectColor
import com.cyrillrx.rpg.core.presentation.theme.WeaponColor
import com.cyrillrx.rpg.magicalitem.domain.MagicalItem

fun MagicalItem.getColor(): Color = when (type) {
    MagicalItem.Type.ARMOR -> ArmorColor
    MagicalItem.Type.POTION -> ObjectColor
    MagicalItem.Type.RING -> ObjectColor
    MagicalItem.Type.ROD -> WeaponColor
    MagicalItem.Type.SCROLL -> ObjectColor
    MagicalItem.Type.STAFF -> WeaponColor
    MagicalItem.Type.WAND -> WeaponColor
    MagicalItem.Type.WEAPON -> WeaponColor
    MagicalItem.Type.WONDROUS_ITEM -> ObjectColor
}
