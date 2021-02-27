package com.cyrillrx.rpg.template

import android.os.Bundle
import com.cyrillrx.rpg.onChange

/**
 * @author Cyril Leroux
 *         Created on 12/04/2020.
 */
abstract class SearchListActivity<Adapter : BaseAdapter<*>> : ListActivity<Adapter>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchView.onChange { query -> query?.let(::applyFilter) }
    }

    abstract fun applyFilter(query: String)
}
