package com.cyrillrx.rpg.xml.game

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cyrillrx.rpg.R
import com.cyrillrx.rpg.models.Game
import com.cyrillrx.rpg.xml.inflate

class GameItemView(parent: ViewGroup) :
    RecyclerView.ViewHolder(parent.inflate(R.layout.item_simple)) {

    private var tvTitle: TextView = itemView.findViewById(R.id.tvTitle)

    fun bind(game: Game) {
        tvTitle.text = game.name
    }
}
