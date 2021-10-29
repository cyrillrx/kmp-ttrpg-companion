package com.cyrillrx.rpg

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cyrillrx.rpg.Router.openDndBestiary
import com.cyrillrx.rpg.Router.openDndInventory
import com.cyrillrx.rpg.Router.openDndSpellBook
import com.cyrillrx.rpg.Router.openDndSpellBookCompose
import com.cyrillrx.rpg.ui.theme.AppTheme
import com.cyrillrx.rpg.ui.theme.spacingMedium

/**
 * @author Cyril Leroux
 *         Created on 30/03/2020.
 */
class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Home()
        }
    }

    @Composable
    fun Home() {
        AppTheme {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(spacingMedium),
            ) {
                HomeButton("Grimoire") { openDndSpellBook() }
                HomeButton("Grimoire Compose") { openDndSpellBookCompose() }
                HomeButton("Bestiaire") { openDndBestiary() }
                HomeButton("Objets magiques") { openDndInventory() }
            }
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
        Home()
    }
}
