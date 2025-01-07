package com.cyrillrx.rpg.character.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.character.domain.PlayerCharacter

@Composable
fun PlayerCharacterDetailScreen(
    character: PlayerCharacter,
    onNavigateUpClicked: () -> Boolean,
) {
    Scaffold { paddingValues ->
        Text(
            text = character.name,
            Modifier.clickable { onNavigateUpClicked() }
                .padding(paddingValues)
                .fillMaxSize(),
        )
    }
}
