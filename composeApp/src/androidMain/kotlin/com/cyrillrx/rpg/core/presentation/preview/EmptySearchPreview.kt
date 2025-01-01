package com.cyrillrx.rpg.core.presentation.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cyrillrx.rpg.core.presentation.component.EmptySearch
import com.cyrillrx.rpg.core.presentation.theme.AppTheme

@Preview(showBackground = true)
@Composable
private fun EmptySearchPreviewLight() {
    AppTheme(darkTheme = false) {
        EmptySearch("abqsmdok")
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptySearchPreviewDark() {
    AppTheme(darkTheme = false) {
        EmptySearch("abqsmdok")
    }
}
