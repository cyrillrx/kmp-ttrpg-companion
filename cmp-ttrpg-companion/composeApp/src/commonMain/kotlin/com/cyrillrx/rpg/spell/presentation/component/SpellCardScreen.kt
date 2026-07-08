package com.cyrillrx.rpg.spell.presentation.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistAdd
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.app.currentLocale
import com.cyrillrx.rpg.core.presentation.component.ErrorLayout
import com.cyrillrx.rpg.core.presentation.component.Loader
import com.cyrillrx.rpg.core.presentation.component.SimpleTopBar
import com.cyrillrx.rpg.core.presentation.state.DetailState
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.spell.data.SampleSpellRepository
import com.cyrillrx.rpg.spell.domain.Spell
import com.cyrillrx.rpg.spell.presentation.SpellAddToListProvider
import com.cyrillrx.rpg.spell.presentation.navigation.SpellRouter
import com.cyrillrx.rpg.spell.presentation.viewmodel.SpellDetailViewModel
import com.cyrillrx.rpg.userlist.data.SampleUserListRepository
import com.cyrillrx.rpg.userlist.presentation.AddToListProvider
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_add_to_list
import rpg_companion.composeapp.generated.resources.error_spell_not_found

@Composable
fun SpellCardScreen(
    viewModel: SpellDetailViewModel,
    router: SpellRouter,
    bottomSheetProvider: AddToListProvider<Spell>,
) {
    val state by viewModel.state.collectAsState()
    when (val s = state) {
        DetailState.Loading -> Loader()
        is DetailState.NotFound -> ErrorLayout(stringResource(Res.string.error_spell_not_found, s.id))
        is DetailState.Found -> SpellDetailScreen(
            spell = s.item,
            onNavigateUpClicked = router::navigateUp,
            addToListProvider = bottomSheetProvider,
        )
    }
}

@Composable
private fun SpellDetailScreen(
    spell: Spell,
    onNavigateUpClicked: () -> Unit,
    addToListProvider: AddToListProvider<Spell>,
) {
    var showAddToListBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            SimpleTopBar(
                title = spell.resolveTranslation(currentLocale()).name,
                onNavigateUpClicked = onNavigateUpClicked,
                actions = {
                    IconButton(onClick = { showAddToListBottomSheet = true }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.PlaylistAdd,
                            contentDescription = stringResource(Res.string.btn_add_to_list),
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        SpellDetail(
            spell = spell,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        )
    }

    if (showAddToListBottomSheet) {
        addToListProvider.BottomSheet(
            entityId = spell.id,
            onDismiss = { showAddToListBottomSheet = false },
        )
    }
}

@Preview
@Composable
fun PreviewSpellCardScreenLight() {
    PreviewSpellCardScreen(darkTheme = false)
}

@Preview
@Composable
fun PreviewSpellCardScreenDark() {
    PreviewSpellCardScreen(darkTheme = true)
}

@Composable
private fun PreviewSpellCardScreen(darkTheme: Boolean) {
    val spell = SampleSpellRepository.fireball()
    val spellRepository = SampleSpellRepository()
    val userListRepository = SampleUserListRepository()
    val bottomSheetProvider = SpellAddToListProvider(spellRepository, userListRepository)

    AppThemePreview(darkTheme = darkTheme) {
        SpellDetailScreen(spell, onNavigateUpClicked = {}, addToListProvider = bottomSheetProvider)
    }
}
