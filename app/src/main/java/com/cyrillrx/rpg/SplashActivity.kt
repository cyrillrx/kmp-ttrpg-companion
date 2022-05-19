package com.cyrillrx.rpg

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cyrillrx.rpg.Router.openHome

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        openHome()
    }
}
