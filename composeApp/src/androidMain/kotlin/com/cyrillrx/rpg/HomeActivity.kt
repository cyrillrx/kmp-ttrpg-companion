package com.cyrillrx.rpg

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cyrillrx.rpg.app.App
import com.cyrillrx.rpg.bestiary.data.JsonBestiaryRepository
import com.cyrillrx.rpg.bestiary.presentation.BestiaryScreen
import com.cyrillrx.rpg.bestiary.presentation.BestiaryViewModel
import com.cyrillrx.rpg.home.presentation.HomeButton
import com.cyrillrx.rpg.home.presentation.HomeRouter
import com.cyrillrx.rpg.home.presentation.HomeScreen
import com.cyrillrx.rpg.xml.bestiary.BestiaryLegacyActivity

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        setContentCommon()
        setContentBestiary()
    }

    private fun setContentCommon() {
        setContent { App() }
    }

    private fun setContentBestiary() {
        setContent {
            Column {
                HomeButton("Bestiaire xml") {
                    val intent = Intent(this@HomeActivity, BestiaryLegacyActivity::class.java)
                    startActivity(intent)
                }

                val viewModel = BestiaryViewModel(JsonBestiaryRepository())
                BestiaryScreen(viewModel.creatures)
            }
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    fun PreviewHomeScreen() {
        HomeScreen(object : HomeRouter {})
    }
}
