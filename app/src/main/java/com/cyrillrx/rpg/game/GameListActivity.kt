package com.cyrillrx.rpg.game

import com.cyrillrx.rpg.template.ListActivity

/**
 * @author Cyril Leroux
 *         Created on 25/09/2019.
 */
class GameListActivity : ListActivity<GameAdapter>() {

    override val adapter = GameAdapter()

    override fun sendRequest() {

        // TODO listOf(Game("", ""))
    }

}