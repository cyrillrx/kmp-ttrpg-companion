package com.cyrillrx.rpg.magicalitem.presentation.component

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
import com.cyrillrx.rpg.magicalitem.data.SampleMagicalItemRepository
import com.cyrillrx.rpg.magicalitem.domain.MagicalItem
import com.cyrillrx.rpg.magicalitem.presentation.MagicalItemAddToCollectionProvider
import com.cyrillrx.rpg.magicalitem.presentation.navigation.MagicalItemRouter
import com.cyrillrx.rpg.magicalitem.presentation.viewmodel.MagicalItemDetailViewModel
import com.cyrillrx.rpg.usercollection.data.SampleUserCollectionRepository
import com.cyrillrx.rpg.usercollection.presentation.AddToCollectionProvider
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_add_to_list
import rpg_companion.composeapp.generated.resources.error_magical_item_not_found

@Composable
fun MagicalItemDetailScreen(
    viewModel: MagicalItemDetailViewModel,
    router: MagicalItemRouter,
    addToCollectionProvider: AddToCollectionProvider<MagicalItem>,
) {
    val state by viewModel.state.collectAsState()
    when (val s = state) {
        DetailState.Loading -> Loader()
        is DetailState.NotFound -> ErrorLayout(stringResource(Res.string.error_magical_item_not_found, s.id))
        is DetailState.Found -> MagicalItemDetailContent(
            magicalItem = s.item,
            onNavigateUpClicked = router::navigateUp,
            addToCollectionProvider = addToCollectionProvider,
        )
    }
}

@Composable
private fun MagicalItemDetailContent(
    magicalItem: MagicalItem,
    onNavigateUpClicked: () -> Unit,
    addToCollectionProvider: AddToCollectionProvider<MagicalItem>,
) {
    var showAddToCollectionBottomSheet by remember { mutableStateOf(false) }

    FadingTitleScaffold(
        title = magicalItem.resolveTranslation(currentLocale()).name,
        onNavigateUpClicked = onNavigateUpClicked,
        actions = {
            IconButton(onClick = { showAddToCollectionBottomSheet = true }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.PlaylistAdd,
                    contentDescription = stringResource(Res.string.btn_add_to_list),
                )
            }
        },
    ) { scrollModifier, titleModifier ->
        MagicalItemDetail(
            magicalItem = magicalItem,
            modifier = scrollModifier,
            titleModifier = titleModifier,
        )
    }

    if (showAddToCollectionBottomSheet) {
        addToCollectionProvider.BottomSheet(
            entityId = magicalItem.id,
            onDismiss = { showAddToCollectionBottomSheet = false },
        )
    }
}

@Preview
@Composable
fun PreviewMagicalItemDetailScreenLight() {
    PreviewMagicalItemDetailScreen(darkTheme = false)
}

@Preview
@Composable
fun PreviewMagicalItemDetailScreenDark() {
    PreviewMagicalItemDetailScreen(darkTheme = true)
}

@Composable
private fun PreviewMagicalItemDetailScreen(darkTheme: Boolean) {
    val magicalItem = SampleMagicalItemRepository.getFirst()
    val addToCollectionProvider =
        MagicalItemAddToCollectionProvider(SampleMagicalItemRepository(), SampleUserCollectionRepository())
    AppThemePreview(darkTheme = darkTheme) {
        MagicalItemDetailContent(
            magicalItem,
            onNavigateUpClicked = {},
            addToCollectionProvider = addToCollectionProvider,
        )
    }
}
