package com.cyrillrx.rpg.creature.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.creature.data.SampleMonsterRepository
import com.cyrillrx.rpg.creature.domain.Monster
import com.cyrillrx.rpg.creature.presentation.MonsterAddToListProvider
import com.cyrillrx.rpg.creature.presentation.navigation.MonsterRouter
import com.cyrillrx.rpg.creature.presentation.viewmodel.MonsterDetailViewModel
import com.cyrillrx.rpg.userlist.data.SampleUserListRepository
import com.cyrillrx.rpg.userlist.presentation.AddToListProvider
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_add_to_list
import rpg_companion.composeapp.generated.resources.error_monster_not_found

@Composable
fun MonsterDetailScreen(
    viewModel: MonsterDetailViewModel,
    router: MonsterRouter,
    addToListProvider: AddToListProvider<Monster>,
) {
    val state by viewModel.state.collectAsState()
    when (val s = state) {
        DetailState.Loading -> Loader()
        is DetailState.NotFound -> ErrorLayout(stringResource(Res.string.error_monster_not_found, s.id))
        is DetailState.Found -> MonsterDetailContent(
            monster = s.item,
            onNavigateUpClicked = router::navigateUp,
            addToListProvider = addToListProvider,
        )
    }
}

@Composable
private fun MonsterDetailContent(
    monster: Monster,
    onNavigateUpClicked: () -> Unit,
    addToListProvider: AddToListProvider<Monster>,
) {
    var showAddToListBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            SimpleTopBar(
                title = monster.resolveTranslation(currentLocale()).name,
                onNavigateUpClicked = onNavigateUpClicked,
            )
        },
        bottomBar = {
            Button(
                onClick = { showAddToListBottomSheet = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacingMedium),
            ) {
                Text(stringResource(Res.string.btn_add_to_list))
            }
        },
    ) { paddingValues ->
        MonsterItem(
            monster = monster,
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
        )
    }

    if (showAddToListBottomSheet) {
        addToListProvider.BottomSheet(
            entityId = monster.id,
            onDismiss = { showAddToListBottomSheet = false },
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
    val addToListProvider = MonsterAddToListProvider(SampleMonsterRepository(), SampleUserListRepository())
    MonsterDetailContent(
        monster = SampleMonsterRepository.getFirst(),
        onNavigateUpClicked = {},
        addToListProvider = addToListProvider,
    )
}
