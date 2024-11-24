package com.cyrillrx.rpg.xml.group

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cyrillrx.rpg.R
import com.cyrillrx.rpg.models.Character
import com.cyrillrx.rpg.xml.inflate

class CharacterItemView(parent: ViewGroup) :
    RecyclerView.ViewHolder(parent.inflate(R.layout.item_simple)) {

    private var tvTitle: TextView = itemView.findViewById(R.id.tvTitle)

    fun bind(character: Character) {
        tvTitle.text = character.name
    }
}
