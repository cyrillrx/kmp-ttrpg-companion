package com.cyrillrx.rpg.settings.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cyrillrx.rpg.core.presentation.component.SimpleTopBar
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.settings.domain.Theme
import com.cyrillrx.rpg.settings.domain.UserPreferences
import com.cyrillrx.rpg.settings.domain.UserPreferencesRepository
import com.cyrillrx.rpg.settings.presentation.navigation.SettingsRouter
import com.cyrillrx.rpg.settings.presentation.viewmodel.SettingsViewModel
import com.cyrillrx.rpg.settings.presentation.viewmodel.SettingsViewModelFactory
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.settings_section_theme
import rpg_companion.composeapp.generated.resources.settings_theme_dark
import rpg_companion.composeapp.generated.resources.settings_theme_light
import rpg_companion.composeapp.generated.resources.settings_theme_system
import rpg_companion.composeapp.generated.resources.title_settings

@Composable
fun SettingsScreen(
    prefsRepository: UserPreferencesRepository,
    router: SettingsRouter,
) {
    val viewModel = viewModel<SettingsViewModel>(factory = SettingsViewModelFactory(prefsRepository))
    val preferences by viewModel.preferences.collectAsState()
    SettingsScreen(
        preferences = preferences,
        onThemeSelected = viewModel::setTheme,
        router = router,
    )
}

@Composable
fun SettingsScreen(
    preferences: UserPreferences,
    onThemeSelected: (Theme) -> Unit,
    router: SettingsRouter,
) {
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
                .padding(paddingValues)
                .padding(spacingCommon),
        ) {
            ThemeSection(
                selectedTheme = preferences.theme,
                onThemeSelected = onThemeSelected,
            )
        }
    }
}

@Composable
private fun ThemeSection(
    selectedTheme: Theme,
    onThemeSelected: (Theme) -> Unit,
) {
    val options = remember {
        listOf(
            Theme.LIGHT to Res.string.settings_theme_light,
            Theme.DARK to Res.string.settings_theme_dark,
            Theme.SYSTEM to Res.string.settings_theme_system,
        )
    }

    Text(
        text = stringResource(Res.string.settings_section_theme),
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(bottom = spacingCommon),
    )
    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        options.forEachIndexed { index, (theme, labelRes) ->
            SegmentedButton(
                selected = selectedTheme == theme,
                onClick = { onThemeSelected(theme) },
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                label = { Text(stringResource(labelRes)) },
            )
        }
    }
}

@Preview
@Composable
private fun PreviewSettingsScreenLight() {
    AppThemePreview(darkTheme = false) {
        SettingsScreen(
            preferences = UserPreferences(theme = Theme.LIGHT),
            onThemeSelected = {},
            router = object : SettingsRouter { override fun navigateUp() {} },
        )
    }
}

@Preview
@Composable
private fun PreviewSettingsScreenDark() {
    AppThemePreview(darkTheme = true) {
        SettingsScreen(
            preferences = UserPreferences(theme = Theme.DARK),
            onThemeSelected = {},
            router = object : SettingsRouter { override fun navigateUp() {} },
        )
    }
}
