package com.cyrillrx.rpg.core.presentation.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cyrillrx.rpg.core.presentation.component.ErrorLayout
import com.cyrillrx.rpg.core.presentation.theme.AppTheme

@Preview(showBackground = true)
@Composable
private fun ErrorPreviewLight() {
    AppTheme(darkTheme = false) {
        ErrorLayout("Dummy error message")
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorPreviewDark() {
    AppTheme(darkTheme = true) {
        ErrorLayout("Dummy error message")
    }
}
