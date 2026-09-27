package com.cyrillrx.rpg.usercollection.presentation

import com.cyrillrx.rpg.core.domain.Entity
import com.cyrillrx.rpg.core.domain.sortedByName

enum class CollectionItemOrder { ADDED, NAME }

fun <T : Entity> List<T>.applyOrder(order: CollectionItemOrder, locale: String): List<T> = when (order) {
    // A collection appends what it is given, so reading its entries back reversed puts the latest first.
    CollectionItemOrder.ADDED -> reversed()
    CollectionItemOrder.NAME -> sortedByName(locale)
}
