package com.cyrillrx.rpg.creature.presentation.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistAdd
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.cyrillrx.rpg.app.currentLocale
import com.cyrillrx.rpg.core.presentation.component.ErrorLayout
import com.cyrillrx.rpg.core.presentation.component.FadingTitleScaffold
import com.cyrillrx.rpg.core.presentation.component.Loader
import com.cyrillrx.rpg.core.presentation.state.DetailState
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.creature.data.SampleMonsterRepository
import com.cyrillrx.rpg.creature.domain.Monster
import com.cyrillrx.rpg.creature.presentation.MonsterAddToCollectionProvider
import com.cyrillrx.rpg.creature.presentation.navigation.MonsterRouter
import com.cyrillrx.rpg.creature.presentation.viewmodel.MonsterDetailViewModel
import com.cyrillrx.rpg.usercollection.data.SampleUserCollectionRepository
import com.cyrillrx.rpg.usercollection.presentation.AddToCollectionProvider
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_add_to_collection
import rpg_companion.composeapp.generated.resources.error_monster_not_found

@Composable
fun MonsterDetailScreen(
    viewModel: MonsterDetailViewModel,
    router: MonsterRouter,
    addToCollectionProvider: AddToCollectionProvider<Monster>,
) {
    val state by viewModel.state.collectAsState()
    when (val s = state) {
        DetailState.Loading -> Loader()
        is DetailState.NotFound -> ErrorLayout(stringResource(Res.string.error_monster_not_found, s.id))
        is DetailState.Found -> MonsterDetailContent(
            monster = s.item,
            onNavigateUpClicked = router::navigateUp,
            addToCollectionProvider = addToCollectionProvider,
        )
    }
}

@Composable
private fun MonsterDetailContent(
    monster: Monster,
    onNavigateUpClicked: () -> Unit,
    addToCollectionProvider: AddToCollectionProvider<Monster>,
) {
    var showAddToCollectionBottomSheet by remember { mutableStateOf(false) }
    val translation = monster.resolveTranslation(currentLocale())

    FadingTitleScaffold(
        title = translation.name,
        onNavigateUpClicked = onNavigateUpClicked,
        actions = {
            IconButton(onClick = { showAddToCollectionBottomSheet = true }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.PlaylistAdd,
                    contentDescription = stringResource(Res.string.btn_add_to_collection),
                )
            }
        },
    ) { scrollModifier, titleModifier ->
        MonsterDetail(
            monster = monster,
            modifier = scrollModifier,
            titleModifier = titleModifier,
            translation = translation,
        )
    }

    if (showAddToCollectionBottomSheet) {
        addToCollectionProvider.BottomSheet(
            entityId = monster.id,
            onDismiss = { showAddToCollectionBottomSheet = false },
        )
    }
}

@Preview
@Composable
private fun PreviewMonsterDetailScreenLight() {
    AppThemePreview(darkTheme = false) { MonsterDetailContentPreview() }
}

@Preview
@Composable
private fun PreviewMonsterDetailScreenDark() {
    AppThemePreview(darkTheme = true) { MonsterDetailContentPreview() }
}

@Composable
private fun MonsterDetailContentPreview() {
    val addToCollectionProvider =
        MonsterAddToCollectionProvider(SampleMonsterRepository(), SampleUserCollectionRepository())
    MonsterDetailContent(
        monster = SampleMonsterRepository.getFirst(),
        onNavigateUpClicked = {},
        addToCollectionProvider = addToCollectionProvider,
    )
}
