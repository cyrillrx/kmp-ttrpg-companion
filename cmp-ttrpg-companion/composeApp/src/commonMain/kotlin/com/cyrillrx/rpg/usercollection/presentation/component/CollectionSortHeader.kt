package com.cyrillrx.rpg.usercollection.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.core.presentation.component.SortHeader
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.usercollection.presentation.CollectionItemOrder
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.sort_added
import rpg_companion.composeapp.generated.resources.sort_name

private val itemOrderOptions = listOf(
    CollectionItemOrder.ADDED to Res.string.sort_added,
    CollectionItemOrder.NAME to Res.string.sort_name,
)

@Composable
fun CollectionSortHeader(
    sortOrder: CollectionItemOrder,
    onSortOrderSelected: (CollectionItemOrder) -> Unit,
    modifier: Modifier = Modifier,
) {
    SortHeader(
        selected = sortOrder,
        options = itemOrderOptions,
        onSelected = onSortOrderSelected,
        modifier = modifier,
    )
}

@Preview
@Composable
private fun PreviewCollectionSortHeaderLight() {
    AppThemePreview(darkTheme = false) {
        CollectionSortHeader(sortOrder = CollectionItemOrder.ADDED, onSortOrderSelected = {})
    }
}

@Preview
@Composable
private fun PreviewCollectionSortHeaderDark() {
    AppThemePreview(darkTheme = true) {
        CollectionSortHeader(sortOrder = CollectionItemOrder.NAME, onSortOrderSelected = {})
    }
}
