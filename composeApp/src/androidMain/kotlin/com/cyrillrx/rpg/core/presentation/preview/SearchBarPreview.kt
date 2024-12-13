package com.cyrillrx.rpg.core.presentation.preview

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cyrillrx.rpg.core.presentation.componenent.SearchBar
import com.cyrillrx.rpg.core.presentation.theme.AppTheme

@Preview
@Composable
private fun SearchBarPreviewLight() {
    SearchBarPreview(darkTheme = false)
}

@Preview
@Composable
private fun SearchBarPreviewDark() {
    SearchBarPreview(darkTheme = true)
}

@Composable
private fun SearchBarPreview(darkTheme: Boolean) {
    AppTheme(darkTheme = darkTheme) {
        SearchBar(
            hint = "Test hint",
            query = "Test Query",
            onQueryChanged = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
