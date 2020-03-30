package com.cyrillrx.rpg.dnd.spellbook

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cyrillrx.rpg.AssetReader
import com.cyrillrx.rpg.R
import com.cyrillrx.rpg.api.spellbook.Spell
import com.cyrillrx.rpg.template.ListActivity
import com.cyrillrx.utils.deserialize
import kotlinx.android.synthetic.main.activity_list.*
import java.util.Locale

/**
 * @author Cyril Leroux
 *         Created on 13/03/2020.
 */
class SpellBookActivity : ListActivity<SpellBookAdapter>() {

    override val adapter = SpellBookAdapter()
    private val locale by lazy { Locale.ROOT }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the options menu from XML
        val inflater = menuInflater
        inflater.inflate(R.menu.search, menu)

        // Get the SearchView and set the searchable configuration
        val searchView = menu?.findItem(R.id.search)?.actionView as? SearchView
        searchView?.apply {
            // Assumes current activity is the searchable activity
            val searchManager = getSystemService(Context.SEARCH_SERVICE) as? SearchManager
            setSearchableInfo(searchManager?.getSearchableInfo(componentName))
            setIconifiedByDefault(false) // Do not iconify the widget; expand it by default

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let(::filterSpellBook)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    newText?.let(::filterSpellBook)
                    return true
                }
            })
        }
        return true
    }

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

    private fun filterSpellBook(query: String) {

        startLoading()

        val serializedSpellBook = AssetReader.readAsString(this, "grimoire.json")
        val spellBook: SpellBook = serializedSpellBook?.deserialize() ?: SpellBook()

        adapter.clear()
        adapter.addAll(spellBook.filter(query))

        stopLoading()
    }

    private fun SpellBook.filter(query: String): SpellBook = filterTo(SpellBook()) { spell -> spell.filter(query) }

    private fun Spell.filter(query: String): Boolean {
        val lowerCaseQuery = query.toLowerCase(locale)
        return title.toLowerCase(locale).contains(lowerCaseQuery)
    }

    class SpellBook : ArrayList<Spell>()
}