package com.cyrillrx.rpg

import android.content.Context
import android.content.Intent
import com.cyrillrx.rpg.api.spellbook.Spell
import com.cyrillrx.rpg.dnd.spellbook.SpellDetailActivity
import com.cyrillrx.rpg.xml.game.GameListLegayActivity
import com.cyrillrx.rpg.xml.group.CharacterListLegacyActivity

object Router {
    fun Context.openSpellDetail(spell: Spell) {
        startActivity(SpellDetailActivity.newIntent(applicationContext, spell))
    }

    fun Context.openGameList() {
        startActivity(Intent(applicationContext, GameListLegayActivity::class.java))
    }

    fun Context.openCharacterList() {
        startActivity(Intent(applicationContext, CharacterListLegacyActivity::class.java))
    }
}
