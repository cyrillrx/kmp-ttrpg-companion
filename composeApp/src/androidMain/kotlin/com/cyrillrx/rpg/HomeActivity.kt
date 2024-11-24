package com.cyrillrx.rpg

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cyrillrx.rpg.Router.openDndBestiary
import com.cyrillrx.rpg.Router.openDndInventory
import com.cyrillrx.rpg.Router.openDndInventoryCompose
import com.cyrillrx.rpg.Router.openDndSpellBook
import com.cyrillrx.rpg.Router.openDndSpellBookCompose
import com.cyrillrx.rpg.presentation.theme.AppTheme
import com.cyrillrx.rpg.presentation.theme.spacingMedium

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
