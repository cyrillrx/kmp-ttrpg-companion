package com.cyrillrx.rpg.dnd.spellbook

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cyrillrx.rpg.AssetReader
import com.cyrillrx.rpg.api.spellbook.Spell
import com.cyrillrx.rpg.template.SearchListActivity
import com.cyrillrx.utils.deserialize
import java.util.Locale

/**
 * @author Cyril Leroux
 *         Created on 13/03/2020.
 */
class SpellBookActivity : SearchListActivity<SpellBookAdapter>() {

    override val adapter = SpellBookAdapter()
    private val locale by lazy { Locale.ROOT }

    override fun createLayoutManager(): RecyclerView.LayoutManager =
        LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

    override fun sendRequest() {

        val spellBook: SpellBook = loadFromFile()
        updateData(spellBook)
    }

    override fun applyFilter(query: String) {

        val spellBook: SpellBook = loadFromFile()
        updateData(spellBook.filter(query))
    }

    private fun updateData(spellBook: SpellBook) {

        startLoading()

        adapter.clear()
        adapter.addAll(spellBook)

        stopLoading()
    }

    private fun loadFromFile(): SpellBook {
        val serializedSpellBook = AssetReader.readAsString(this, "grimoire.json")
        return serializedSpellBook?.deserialize() ?: SpellBook()
    }

    private fun SpellBook.filter(query: String): SpellBook = filterTo(SpellBook()) { spell -> spell.filter(query) }

    private fun Spell.filter(query: String): Boolean {
        val lowerCaseQuery = query.toLowerCase(locale)
        return title.toLowerCase(locale).contains(lowerCaseQuery) ||
                content.toLowerCase(locale).contains(lowerCaseQuery) ||
                lowerCaseQuery in getSpellClasses().map { it.toLowerCase(locale) }
    }

    class SpellBook : ArrayList<Spell>()
}