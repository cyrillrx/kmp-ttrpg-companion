package com.cyrillrx.rpg.game

import com.cyrillrx.rpg.models.Game
import com.cyrillrx.rpg.template.ListActivity

/**
 * @author Cyril Leroux
 *         Created on 25/09/2019.
 */
class GameListActivity : ListActivity<GameAdapter>() {

    override val adapter = GameAdapter()

    override fun sendRequest() {

        startLoading()

        val games = listOf(
            Game("hero_dragon", "Héros et Dragons"),
            Game("equestria", "Tales of Equestria"),
            Game("sw_edge", "Edge of the Empire"),
            Game("sw_rebellion", "Age of Rebellion"),
            Game("sw_force", "Force and Destiny")
        )

        adapter.addAll(games)

        stopLoading()
    }
}