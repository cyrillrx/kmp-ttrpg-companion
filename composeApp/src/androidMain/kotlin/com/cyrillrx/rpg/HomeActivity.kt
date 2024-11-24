package com.cyrillrx.rpg

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cyrillrx.rpg.dnd.inventory.InventoryActivity
import com.cyrillrx.rpg.dnd.spellbook.SpellBookActivity
import com.cyrillrx.rpg.presentation.home.HomeRouter
import com.cyrillrx.rpg.presentation.home.HomeScreen
import com.cyrillrx.rpg.presentation.theme.AppTheme
import com.cyrillrx.rpg.xml.bestiary.BestiaryLegacyActivity
import com.cyrillrx.rpg.xml.spellbook.SpellBookLegacyActivity

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val router = Router(this)
            AppTheme { HomeScreen(router) }
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    fun PreviewHomeScreen() {
        HomeScreen(object : HomeRouter {})
    }

    class Router(private val context: Context) : HomeRouter {
        override fun openSpellBook() {
            val intent = Intent(context.applicationContext, SpellBookActivity::class.java)
            context.startActivity(intent)
        }

        override fun openBestiary() {
            // TODO: Implement bestiary in Compose
        }

        override fun openInventory() {
            val intent = Intent(context.applicationContext, InventoryActivity::class.java)
            context.startActivity(intent)
        }

        override fun openLegacySpellBook() {
            val intent = Intent(context.applicationContext, SpellBookLegacyActivity::class.java)
            context.startActivity(intent)
        }

        override fun openLegacyBestiary() {
            val intent = Intent(context.applicationContext, BestiaryLegacyActivity::class.java)
            context.startActivity(intent)
        }

        override fun openLegacyInventory() {
            val intent = Intent(context.applicationContext, InventoryActivity::class.java)
            context.startActivity(intent)
        }
    }
}
