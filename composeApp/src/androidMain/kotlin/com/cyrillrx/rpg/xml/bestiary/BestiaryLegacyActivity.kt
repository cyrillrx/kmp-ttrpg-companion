package com.cyrillrx.rpg.xml.bestiary

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cyrillrx.rpg.R
import com.cyrillrx.rpg.bestiary.data.JsonBestiaryRepository
import kotlinx.coroutines.launch

class BestiaryLegacyActivity : AppCompatActivity() {

    private lateinit var placeholderLayout: PlaceholderLayout
    private lateinit var loader: View

    private val adapter = BestiaryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        placeholderLayout = findViewById(R.id.empty_layout)
        loader = findViewById(R.id.loader)

        setupRecycler(findViewById(R.id.recycler))

        sendRequest()
    }

    private fun setupRecycler(recyclerView: RecyclerView) {
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    private fun startLoading() {
        loader.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        loader.visibility = View.GONE
    }

    private fun sendRequest() {
        lifecycleScope.launch {
            startLoading()

            val creatures = JsonBestiaryRepository().getAll()
            adapter.addAll(creatures)

            stopLoading()
        }
    }
}
