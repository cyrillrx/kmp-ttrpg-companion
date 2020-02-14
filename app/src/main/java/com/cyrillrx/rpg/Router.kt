package com.cyrillrx.rpg

import android.content.Context
import android.content.Intent
import com.cyrillrx.rpg.game.GameListActivity
import com.cyrillrx.rpg.group.CharacterListActivity

/**
 * @author Cyril Leroux
 *         Created on 25/09/2019.
 */
object Router {

    fun Context.openGameList() {
        startActivity(Intent(applicationContext, GameListActivity::class.java))
    }

    fun Context.openCharacterList() {
        startActivity(Intent(applicationContext, CharacterListActivity::class.java))
    }
}