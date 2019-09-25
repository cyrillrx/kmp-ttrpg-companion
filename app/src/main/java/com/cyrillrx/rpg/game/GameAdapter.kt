package com.cyrillrx.rpg.game

import android.view.ViewGroup
import com.cyrillrx.rpg.models.Game
import com.cyrillrx.rpg.template.BaseAdapter

/**
 * @author Cyril Leroux
 *         Created on 24/09/2019.
 */
class GameAdapter : BaseAdapter<GameItemView>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameItemView = GameItemView(parent)

    override fun onBindViewHolder(holder: GameItemView, position: Int) {
        holder.bind(getItem(position) as Game)
    }
}