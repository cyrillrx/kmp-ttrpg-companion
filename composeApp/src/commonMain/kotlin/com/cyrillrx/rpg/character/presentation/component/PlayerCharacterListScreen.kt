package com.cyrillrx.rpg.character.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.character.presentation.viewmodel.PlayerCharacterListViewModel

@Composable
fun PlayerCharacterListScreen(viewModel: PlayerCharacterListViewModel) {
    Scaffold { paddingValues ->
        Text(
            text = "Player Character List Screen",
            Modifier.clickable { viewModel.onNavigateUpClicked() }
                .padding(paddingValues)
                .fillMaxSize(),
        )
    }
}
