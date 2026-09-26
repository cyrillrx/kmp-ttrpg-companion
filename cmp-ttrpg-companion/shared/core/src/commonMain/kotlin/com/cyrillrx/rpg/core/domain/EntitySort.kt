package com.cyrillrx.rpg.core.domain

import com.cyrillrx.core.domain.localizedSortKey

enum class StoredSortOrder { LAST_MODIFIED, NAME }

fun <T : Entity> List<T>.sortedByName(locale: String): List<T> =
    sortedWith(compareBy<T> { it.displayName(locale).localizedSortKey() }.thenBy { it.id })

fun <T : Entity> List<Stored<T>>.applySort(order: StoredSortOrder, locale: String): List<Stored<T>> =
    when (order) {
        StoredSortOrder.LAST_MODIFIED ->
            sortedWith(compareByDescending<Stored<T>> { it.updatedAt }.thenBy { it.value.id })

        StoredSortOrder.NAME ->
            sortedWith(
                compareBy<Stored<T>> { it.value.displayName(locale).localizedSortKey() }.thenBy { it.value.id },
            )
    }
