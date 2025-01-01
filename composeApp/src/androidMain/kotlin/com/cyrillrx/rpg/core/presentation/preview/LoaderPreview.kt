package com.cyrillrx.rpg.core.presentation.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cyrillrx.rpg.core.presentation.component.Loader
import com.cyrillrx.rpg.core.presentation.theme.AppTheme

@Preview(showBackground = true)
@Composable
private fun LoaderPreviewLight() {
    AppTheme(darkTheme = false) {
        Loader()
    }
}

@Preview(showBackground = true)
@Composable
private fun LoaderPreviewDark() {
    AppTheme(darkTheme = true) {
        Loader()
    }
}
