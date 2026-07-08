package com.cyrillrx.rpg.spell.presentation.component

import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
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
fun SpellDetailScreen(
    viewModel: SpellDetailViewModel,
    router: SpellRouter,
    bottomSheetProvider: AddToListProvider<Spell>,
) {
    val state by viewModel.state.collectAsState()
    when (val s = state) {
        DetailState.Loading -> Loader()
        is DetailState.NotFound -> ErrorLayout(stringResource(Res.string.error_spell_not_found, s.id))
        is DetailState.Found -> SpellDetailContent(
            spell = s.item,
            onNavigateUpClicked = router::navigateUp,
            addToListProvider = bottomSheetProvider,
        )
    }
}

@Composable
private fun SpellDetailContent(
    spell: Spell,
    onNavigateUpClicked: () -> Unit,
    addToListProvider: AddToListProvider<Spell>,
) {
    var showAddToListBottomSheet by remember { mutableStateOf(false) }

    // Reveal the top-bar title as the header name scrolls up: alpha ramps from 0 (no
    // scroll) to 1 when the scroll offset reaches the name's bottom (name behind the bar).
    val scrollState = rememberScrollState()
    var contentTop by remember { mutableFloatStateOf(0f) }
    var titleBottomOffset by remember { mutableFloatStateOf(0f) }
    val titleAlpha by remember {
        derivedStateOf {
            if (titleBottomOffset <= 0f) 0f else (scrollState.value / titleBottomOffset).coerceIn(0f, 1f)
        }
    }

    Scaffold(
        topBar = {
            SimpleTopBar(
                title = spell.resolveTranslation(currentLocale()).name,
                onNavigateUpClicked = onNavigateUpClicked,
                titleAlpha = titleAlpha,
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
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .onGloballyPositioned { contentTop = it.positionInRoot().y },
        ) {
            SpellDetail(
                spell = spell,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                titleModifier = Modifier.onGloballyPositioned {
                    // Name bottom offset from the content top, stable across scroll.
                    // positionInRoot is unclipped (unlike boundsInRoot, which clamps once
                    // the name scrolls out of the viewport and corrupts the value).
                    titleBottomOffset = it.positionInRoot().y + it.size.height - contentTop + scrollState.value
                },
            )
        }
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
fun PreviewSpellDetailScreenLight() {
    PreviewSpellDetailScreen(darkTheme = false)
}

@Preview
@Composable
fun PreviewSpellDetailScreenDark() {
    PreviewSpellDetailScreen(darkTheme = true)
}

@Composable
private fun PreviewSpellDetailScreen(darkTheme: Boolean) {
    val spell = SampleSpellRepository.fireball()
    val spellRepository = SampleSpellRepository()
    val userListRepository = SampleUserListRepository()
    val bottomSheetProvider = SpellAddToListProvider(spellRepository, userListRepository)

    AppThemePreview(darkTheme = darkTheme) {
        SpellDetailContent(spell, onNavigateUpClicked = {}, addToListProvider = bottomSheetProvider)
    }
}
