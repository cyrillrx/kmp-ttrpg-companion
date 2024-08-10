package com.cyrillrx.rpg.template

import androidx.recyclerview.widget.RecyclerView
import java.util.*

abstract class BaseAdapter<ItemView : RecyclerView.ViewHolder> : RecyclerView.Adapter<ItemView>() {

    private val items: ArrayList<Any> = ArrayList()

    override fun getItemCount(): Int = items.size

    fun add(newItem: Any) {

        items.add(newItem)
        notifyItemInserted(items.size - 1)
    }

    fun addAll(newItems: Collection<Any>) {

        val startPos = items.size
        items.addAll(newItems)
        notifyItemRangeInserted(startPos, newItems.size)
    }

    fun clear() {

        items.clear()
        notifyDataSetChanged()
    }

    protected fun getItem(position: Int) = items[position]
}
