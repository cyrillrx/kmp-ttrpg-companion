package com.cyrillrx.rpg.xml.spellbook

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cyrillrx.rpg.AssetReader
import com.cyrillrx.rpg.api.spellbook.ApiSpell
import com.cyrillrx.rpg.xml.template.SearchListActivity
import com.cyrillrx.utils.deserialize
import java.util.Locale

class SpellBookLegacyActivity : SearchListActivity<SpellBookAdapter>() {

    override val adapter = SpellBookAdapter()
    private val locale by lazy { Locale.ROOT }

    override fun createLayoutManager(): RecyclerView.LayoutManager =
        LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

    override fun sendRequest() {
        val spells: List<ApiSpell> = loadFromFile()
        updateData(spells)
    }

    override fun applyFilter(query: String) {
        val spells: List<ApiSpell> = loadFromFile()
        updateData(spells.filter(query))
    }

    private fun updateData(spells: List<ApiSpell>) {
        startLoading()

        adapter.clear()
        adapter.addAll(spells)

        stopLoading()
    }

    private fun loadFromFile(): List<ApiSpell> {
        val serializedSpellBook = AssetReader.readAsString(this, "grimoire.json")
        val spellBook: List<ApiSpell> = serializedSpellBook?.deserialize() ?: listOf()
        return spellBook
    }

    private fun List<ApiSpell>.filter(query: String): List<ApiSpell> =
        filterTo(ArrayList()) { spell -> spell.filter(query) }

    private fun ApiSpell.filter(query: String): Boolean {
        val lowerCaseQuery = query.trim().lowercase(locale)
        return title.lowercase(locale).contains(lowerCaseQuery) ||
            content.lowercase(locale).contains(lowerCaseQuery) ||
            lowerCaseQuery in getSpellClasses().map { it.lowercase(locale) }
    }
}
