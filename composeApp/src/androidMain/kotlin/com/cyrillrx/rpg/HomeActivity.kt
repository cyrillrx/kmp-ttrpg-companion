package com.cyrillrx.rpg

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cyrillrx.rpg.app.App
import com.cyrillrx.rpg.home.presentation.HomeRouter
import com.cyrillrx.rpg.home.presentation.HomeScreen

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App()

//            val router = object : HomeRouter {
//                override fun openBestiary() {
//                    val intent = Intent(this@HomeActivity, BestiaryLegacyActivity::class.java)
//                    startActivity(intent)
//                }
//            }
//            AppTheme { HomeScreen(router) }
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    fun PreviewHomeScreen() {
        HomeScreen(object : HomeRouter {})
    }
}
