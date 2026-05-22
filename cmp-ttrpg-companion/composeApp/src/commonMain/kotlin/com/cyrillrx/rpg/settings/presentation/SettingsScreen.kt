package com.cyrillrx.rpg.settings.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.core.presentation.component.SimpleTopBar
import com.cyrillrx.rpg.settings.presentation.navigation.SettingsRouter
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.title_settings

@Composable
fun SettingsScreen(router: SettingsRouter) {
    Scaffold(
        topBar = {
            SimpleTopBar(
                titleResource = Res.string.title_settings,
                onNavigateUpClicked = router::navigateUp,
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            // Settings content added in subsequent PRs
        }
    }
}
