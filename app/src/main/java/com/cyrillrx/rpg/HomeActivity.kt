package com.cyrillrx.rpg

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.cyrillrx.rpg.Router.openDndBestiary
import com.cyrillrx.rpg.Router.openDndInventory
import com.cyrillrx.rpg.Router.openDndSpellBook

/**
 * @author Cyril Leroux
 *         Created on 30/03/2020.
 */
class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        findViewById<View>(R.id.btnSpellbook).setOnClickListener { openDndSpellBook() }
        findViewById<View>(R.id.btnBestiary).setOnClickListener { openDndBestiary() }
        findViewById<View>(R.id.btnInventory).setOnClickListener { openDndInventory() }
    }
}
