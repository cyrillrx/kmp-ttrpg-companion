package com.cyrillrx.rpg.template

import android.app.SearchManager
import android.content.Context
import android.view.Menu
import androidx.appcompat.widget.SearchView
import com.cyrillrx.rpg.R

/**
 * @author Cyril Leroux
 *         Created on 12/04/2020.
 */
abstract class SearchListActivity<Adapter : BaseAdapter<*>> : ListActivity<Adapter>() {

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
                    query?.let(::applyFilter)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    newText?.let(::applyFilter)
                    return true
                }
            })
        }
        return true
    }

    abstract fun applyFilter(query: String)
}