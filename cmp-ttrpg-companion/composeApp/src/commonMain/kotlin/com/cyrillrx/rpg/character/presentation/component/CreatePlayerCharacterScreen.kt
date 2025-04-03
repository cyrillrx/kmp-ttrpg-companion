package com.cyrillrx.rpg.character.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CreatePlayerCharacterScreen(
    onNavigateUpClicked: () -> Unit,
) {
    Scaffold { paddingValues ->
        Text(
            text = "Create Player Character Screen",
            Modifier.clickable { onNavigateUpClicked() }
                .padding(paddingValues)
                .fillMaxSize(),
        )
    }
}
