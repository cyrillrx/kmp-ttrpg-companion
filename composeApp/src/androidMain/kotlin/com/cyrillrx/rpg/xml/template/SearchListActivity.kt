package com.cyrillrx.rpg.xml.template

import android.os.Bundle
import com.cyrillrx.rpg.onChange

abstract class SearchListActivity<Adapter : BaseAdapter<*>> : ListActivity<Adapter>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchView.onChange { query -> query?.let(::applyFilter) }
    }

    abstract fun applyFilter(query: String)
}
