package com.cyrillrx.rpg.game

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cyrillrx.android.utils.inflate
import com.cyrillrx.rpg.R
import com.cyrillrx.rpg.models.Game
import kotlinx.android.synthetic.main.item_game.view.*

/**
 * @author Cyril Leroux
 *         Created on 24/09/2019.
 */
class GameItemView (parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_game)) {

    fun bind(game: Game){
        itemView.tvTitle.text = game.name
    }
}