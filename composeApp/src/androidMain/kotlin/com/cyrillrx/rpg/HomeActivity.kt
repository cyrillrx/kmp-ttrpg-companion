package com.cyrillrx.rpg

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cyrillrx.rpg.common.theme.AppTheme
import com.cyrillrx.rpg.dnd.inventory.InventoryActivity
import com.cyrillrx.rpg.home.presentation.HomeRouter
import com.cyrillrx.rpg.home.presentation.HomeScreen
import com.cyrillrx.rpg.xml.bestiary.BestiaryLegacyActivity

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
//            App()
            val router = Router(this)
            AppTheme() { HomeScreen(router) }
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    fun PreviewHomeScreen() {
        HomeScreen(object : HomeRouter {})
    }

    class Router(private val context: Context) : HomeRouter {
        override fun openMagicalItems() {
            val intent = Intent(context.applicationContext, InventoryActivity::class.java)
            context.startActivity(intent)
        }

        override fun openLegacyBestiary() {
            val intent = Intent(context.applicationContext, BestiaryLegacyActivity::class.java)
            context.startActivity(intent)
        }
    }
}
