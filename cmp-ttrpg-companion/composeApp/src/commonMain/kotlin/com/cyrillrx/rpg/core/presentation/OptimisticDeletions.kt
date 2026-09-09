package com.cyrillrx.rpg.core.presentation

/**
 * Holds the last list read from the repository together with the entries hidden from it, so [visible]
 * can never be derived from one half updated without the other.
 *
 * A hidden entry goes through two stages: [hide] makes it undoable, [claim] takes it one-shot so a
 * snackbar that expires and an undo that arrives late cannot both act on it. It stays out of [visible]
 * while claimed, which is what keeps the row off screen for the whole repository call rather than only
 * until the claim is taken.
 *
 * [identity] recognises an item across two reads. Structural equality is not enough: the data layer is
 * free to rebuild its entities, and two reads would then yield unequal instances of the same row.
 */
class OptimisticDeletions<Item>(private val identity: (Item) -> Any) {

    /** Compared by reference, so [undo] and [claim] act on exactly the entry they were handed. */
    class Pending<T> internal constructor(val item: T)

    private val hidden = mutableListOf<Pending<Item>>()
    private val claimed = mutableListOf<Pending<Item>>()
    private var loaded: List<Item> = emptyList()

    val visible: List<Item>
        get() {
            val hiddenIds = hiddenIds()
            return loaded.filterNot { identity(it) in hiddenIds }
        }

    private fun hiddenIds(): Set<Any> {
        val ids = mutableSetOf<Any>()
        hidden.mapTo(ids) { identity(it.item) }
        claimed.mapTo(ids) { identity(it.item) }
        return ids
    }

    fun setLoaded(items: List<Item>) {
        loaded = items
    }

    /** Null when [item] is not on screen, so a row the caller read from a stale list cannot be hidden. */
    fun hide(item: Item): Pending<Item>? {
        if (visible.none { identity(it) == identity(item) }) return null

        return Pending<Item>(item).also { hidden.add(it) }
    }

    fun undo(pending: Pending<Item>): Boolean = hidden.remove(pending)

    fun claim(pending: Pending<Item>): Boolean {
        if (!hidden.remove(pending)) return false

        claimed.add(pending)
        return true
    }

    fun claimAll(): List<Pending<Item>> {
        val claiming = hidden.toList()
        hidden.clear()
        claimed.addAll(claiming)
        return claiming
    }

    /** Ends a claim: [deleted] drops the item from the loaded list, otherwise it becomes visible again. */
    fun settle(pending: Pending<Item>, deleted: Boolean) {
        if (!claimed.remove(pending)) return
        if (!deleted) return

        val deletedId = identity(pending.item)
        loaded = loaded.filterNot { identity(it) == deletedId }
    }
}
