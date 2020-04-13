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

        setSupportActionBar(findViewById(R.id.toolbar))

        setupRecycler(findViewById(R.id.recycler))

        sendRequest()
    }

    protected open fun setupRecycler(recyclerView: RecyclerView) {

        val layoutManager = createLayoutManager()
        recyclerView.layoutManager = layoutManager
        addItemDecoration(recyclerView, layoutManager)

        recyclerView.adapter = adapter

        refreshLayout.setOnRefreshListener {
            adapter.clear()
            sendRequest()
        }
    }

    protected open fun createLayoutManager(): RecyclerView.LayoutManager = LinearLayoutManager(this)

    protected open fun addItemDecoration(recyclerView: RecyclerView, layoutManager: RecyclerView.LayoutManager) {}

    protected open fun startLoading() {
        loader.visibility = View.VISIBLE
    }

    protected open fun stopLoading() {
        refreshLayout.isRefreshing = false
        loader.visibility = View.GONE
    }

    protected abstract fun sendRequest()
}