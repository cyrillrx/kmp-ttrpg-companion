package com.cyrillrx.rpg

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cyrillrx.rpg.Router.openDndSpellBook

/**
 * @author Cyril Leroux
 *         Created on 24/09/2019.
 */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // openDndBestiary()
        openDndSpellBook()
    }
}