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
            AppTheme { HomeScreen() }
        }
    }

    @Composable
    fun HomeScreen() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(spacingMedium),
        ) {
            HomeButton("Grimoire") { openDndSpellBook() }
            HomeButton("Grimoire Compose") { openDndSpellBookCompose() }
            HomeButton("Bestiaire") { openDndBestiary() }
            HomeButton("Objets magiques") { openDndInventory() }
            HomeButton("Objets magiques Compose") { openDndInventoryCompose() }
        }
    }

    @Composable
    fun HomeButton(text: String, onClick: () -> Unit) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(PaddingValues(spacingMedium)),
        ) {
            Text(text = text)
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    fun PreviewMessageCard() {
        HomeScreen()
    }
}
