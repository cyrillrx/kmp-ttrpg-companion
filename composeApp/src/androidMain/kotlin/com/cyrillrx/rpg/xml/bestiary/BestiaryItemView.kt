package com.cyrillrx.rpg.xml.bestiary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.cyrillrx.rpg.R
import com.cyrillrx.rpg.models.bestiary.Creature
import com.cyrillrx.rpg.setHtmlText

class BestiaryItemView(parent: ViewGroup) :
    RecyclerView.ViewHolder(parent.inflate(R.layout.item_bestiary)) {

    private var tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
    private var tvContent: TextView = itemView.findViewById(R.id.tvContent)
    private var abilitiesView: AbilitiesView = itemView.findViewById(R.id.abilitiesLayout)

    fun bind(creature: Creature) {
        tvTitle.text = creature.name
        tvContent.setHtmlText(creature.description)
        abilitiesView.setAbilities(creature.abilities)
    }

    companion object {
        fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View =
            LayoutInflater
                .from(this.context)
                .inflate(layoutRes, this, attachToRoot)
    }
}
