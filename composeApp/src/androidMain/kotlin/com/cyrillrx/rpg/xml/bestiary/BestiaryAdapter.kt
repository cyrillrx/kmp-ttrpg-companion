package com.cyrillrx.rpg.xml.bestiary

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cyrillrx.rpg.models.bestiary.Creature

class BestiaryAdapter : RecyclerView.Adapter<BestiaryItemView>() {

    private val items: MutableList<Creature> = ArrayList()

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BestiaryItemView(parent)

    override fun onBindViewHolder(holder: BestiaryItemView, position: Int) {
        holder.bind(items[position])
    }

    fun addAll(newCreatures: Collection<Creature>) {
        val startPos = items.size
        items.addAll(newCreatures)
        notifyItemRangeInserted(startPos, newCreatures.size)
    }
}
