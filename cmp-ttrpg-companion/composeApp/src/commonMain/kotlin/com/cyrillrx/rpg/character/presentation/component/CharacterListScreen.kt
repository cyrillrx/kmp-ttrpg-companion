package com.cyrillrx.rpg.character.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.character.presentation.navigation.CharacterRouter
import com.cyrillrx.rpg.character.presentation.viewmodel.CharacterListViewModel

@Composable
fun CharacterListScreen(viewModel: CharacterListViewModel, router: CharacterRouter) {
    Scaffold { paddingValues ->
        Text(
            text = "Player Character List Screen",
            Modifier.clickable(onClick = router::navigateUp)
                .padding(paddingValues)
                .fillMaxSize(),
        )
    }
}
