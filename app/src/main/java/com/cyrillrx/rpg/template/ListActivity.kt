package com.cyrillrx.rpg.template

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cyrillrx.rpg.R
import com.cyrillrx.templates.layout.PlaceholderLayout

/**
 * @author Cyril Leroux
 *         Created on 25/09/2019.
 */
abstract class ListActivity<Adapter : BaseAdapter<*>> : AppCompatActivity() {

    protected lateinit var refreshLayout: SwipeRefreshLayout
    protected lateinit var placeholderLayout: PlaceholderLayout
    protected lateinit var loader: View

    protected abstract val adapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        refreshLayout = findViewById(R.id.swipe_refresh)
        placeholderLayout = findViewById(R.id.empty_layout)
        loader = findViewById(R.id.loader)

        setupRecycler(findViewById(R.id.recycler))

        sendRequest()
    }

    protected open fun setupRecycler(recyclerView: RecyclerView) {

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        addItemDecoration(recyclerView, layoutManager)

        recyclerView.adapter = adapter
    }

    protected open fun addItemDecoration(recyclerView: RecyclerView, layoutManager: LinearLayoutManager) {}

    protected abstract fun sendRequest()
}