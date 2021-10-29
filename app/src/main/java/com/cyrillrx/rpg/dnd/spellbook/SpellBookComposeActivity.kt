package com.cyrillrx.rpg.dnd.spellbook

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.cyrillrx.rpg.AssetReader
import com.cyrillrx.rpg.api.spellbook.Spell
import com.cyrillrx.utils.deserialize

/**
 * @author Cyril Leroux
 *         Created on 29/10/2021.
 */
class SpellBookComposeActivity : AppCompatActivity() {

    private val viewModel by viewModels<SpellBookViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SpellBookScreen(viewModel.spells, viewModel.query, viewModel::applyFilter)
        }

        viewModel.init(loadFromFile())
    }

    private fun loadFromFile(): List<Spell> {
        val serializedSpellBook = AssetReader.readAsString(this, "grimoire.json")
        return serializedSpellBook?.deserialize<SpellBook>() ?: SpellBook()
    }

    private class SpellBook : ArrayList<Spell>()
}
