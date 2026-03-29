package com.cyrillrx.rpg.core.presentation.component

import androidx.compose.runtime.Composable
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.no_result_found
import rpg_companion.composeapp.generated.resources.no_result_found_for_query

@Composable
fun EmptySearch(searchQuery: String) {
    val text = if (searchQuery.isBlank()) {
        stringResource(Res.string.no_result_found)
    } else {
        stringResource(Res.string.no_result_found_for_query, searchQuery)
    }
    ErrorLayout(text)
}

@Preview
@Composable
private fun PreviewEmptySearchLight() {
    AppThemePreview(darkTheme = false) {
        EmptySearch("abqsmdok")
    }
}

@Preview
@Composable
private fun PreviewEmptySearchDark() {
    AppThemePreview(darkTheme = true) {
        EmptySearch("abqsmdok")
    }
}
