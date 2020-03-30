package com.cyrillrx.rpg

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cyrillrx.rpg.Router.openDndBestiary
import com.cyrillrx.rpg.Router.openDndInventory
import com.cyrillrx.rpg.Router.openDndSpellBook
import kotlinx.android.synthetic.main.activity_home.*

/**
 * @author Cyril Leroux
 *         Created on 30/03/2020.
 */
class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btnSpellbook.setOnClickListener { openDndSpellBook() }
        btnBestiary.setOnClickListener { openDndBestiary() }
        btnInventory.setOnClickListener { openDndInventory() }
    }
}