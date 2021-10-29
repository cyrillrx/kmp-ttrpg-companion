package com.cyrillrx.rpg

import android.content.Context
import android.content.Intent
import com.cyrillrx.rpg.dnd.bestiary.BestiaryActivity
import com.cyrillrx.rpg.dnd.inventory.InventoryActivity
import com.cyrillrx.rpg.dnd.spellbook.SpellBookActivity
import com.cyrillrx.rpg.dnd.spellbook.SpellBookComposeActivity
import com.cyrillrx.rpg.game.GameListActivity
import com.cyrillrx.rpg.group.CharacterListActivity

/**
 * @author Cyril Leroux
 *         Created on 25/09/2019.
 */
object Router {

    fun Context.openHome() {
        val intent = Intent(applicationContext, HomeActivity::class.java)
            .apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
        startActivity(intent)
    }

    fun Context.openDndSpellBook() {
        startActivity(Intent(applicationContext, SpellBookActivity::class.java))
    }

    fun Context.openDndSpellBookCompose() {
        startActivity(Intent(applicationContext, SpellBookComposeActivity::class.java))
    }

    fun Context.openDndBestiary() {
        startActivity(Intent(applicationContext, BestiaryActivity::class.java))
    }

    fun Context.openDndInventory() {
        startActivity(Intent(applicationContext, InventoryActivity::class.java))
    }

    fun Context.openGameList() {
        startActivity(Intent(applicationContext, GameListActivity::class.java))
    }

    fun Context.openCharacterList() {
        startActivity(Intent(applicationContext, CharacterListActivity::class.java))
    }
}
