package com.cyrillrx.rpg.xml.game

import android.view.ViewGroup
import com.cyrillrx.rpg.models.Game
import com.cyrillrx.rpg.template.BaseAdapter

class GameAdapter : BaseAdapter<GameItemView>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = GameItemView(parent)

    override fun onBindViewHolder(holder: GameItemView, position: Int) {
        holder.bind(getItem(position) as Game)
    }
}
