package com.cyrillrx.rpg.dnd.spellbook

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cyrillrx.rpg.AssetReader
import com.cyrillrx.rpg.api.spellbook.Spell
import com.cyrillrx.rpg.template.ListActivity
import com.cyrillrx.utils.deserialize

/**
 * @author Cyril Leroux
 *         Created on 13/03/2020.
 */
class SpellBookActivity : ListActivity<SpellBookAdapter>() {

    override val adapter = SpellBookAdapter()

    override fun setupRecycler(recyclerView: RecyclerView) {
        super.setupRecycler(recyclerView)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
    }

    override fun sendRequest() {

        startLoading()

        val serializedSpellBook = AssetReader.readAsString(this, "grimoire.json")
        val spellBook: SpellBook = serializedSpellBook?.deserialize() ?: SpellBook()

        adapter.addAll(spellBook)

        stopLoading()
    }

    class SpellBook : ArrayList<Spell>()
}