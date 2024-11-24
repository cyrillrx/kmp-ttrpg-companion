package com.cyrillrx.rpg.xml.spellbook

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cyrillrx.rpg.AssetReader
import com.cyrillrx.rpg.api.spellbook.Spell
import com.cyrillrx.rpg.template.SearchListActivity
import com.cyrillrx.utils.deserialize
import java.util.Locale

class SpellBookLegacyActivity : SearchListActivity<SpellBookAdapter>() {

    override val adapter = SpellBookAdapter()
    private val locale by lazy { Locale.ROOT }

    override fun createLayoutManager(): RecyclerView.LayoutManager =
        LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

    override fun sendRequest() {

        val spells: List<Spell> = loadFromFile()
        updateData(spells)
    }

    override fun applyFilter(query: String) {

        val spells: List<Spell> = loadFromFile()
        updateData(spells.filter(query))
    }

    private fun updateData(spells: List<Spell>) {

        startLoading()

        adapter.clear()
        adapter.addAll(spells)

        stopLoading()
    }

    private fun loadFromFile(): List<Spell> {
        val serializedSpellBook = AssetReader.readAsString(this, "grimoire.json")
        val spellBook: List<Spell> = serializedSpellBook?.deserialize() ?: listOf()
        return spellBook
    }

    private fun List<Spell>.filter(query: String): List<Spell> =
        filterTo(ArrayList()) { spell -> spell.filter(query) }

    private fun Spell.filter(query: String): Boolean {
        val lowerCaseQuery = query.trim().lowercase(locale)
        return title.lowercase(locale).contains(lowerCaseQuery) ||
                content.lowercase(locale).contains(lowerCaseQuery) ||
                lowerCaseQuery in getSpellClasses().map { it.lowercase(locale) }
    }
}
