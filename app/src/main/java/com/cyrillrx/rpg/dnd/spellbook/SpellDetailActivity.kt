package com.cyrillrx.rpg.dnd.spellbook

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.cyrillrx.rpg.api.spellbook.Spell
import com.cyrillrx.rpg.ui.theme.AppTheme
import com.cyrillrx.utils.deserialize
import com.cyrillrx.utils.serialize

class SpellDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val spell: Spell = intent.getStringExtra(ARG_SPELL)!!.deserialize()
        setContent {
            AppTheme { SpellCard(spell) }
        }
    }

    companion object {
        private const val ARG_SPELL = "spell"

        fun newIntent(context: Context, spell: Spell) =
            Intent(context, SpellDetailActivity::class.java)
                .apply { putExtra(ARG_SPELL, spell.serialize()) }
    }
}
