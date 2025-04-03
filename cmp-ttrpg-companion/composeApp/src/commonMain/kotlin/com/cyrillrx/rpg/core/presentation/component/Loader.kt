package com.cyrillrx.rpg.core.presentation.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Loader() {
    Text(
        text = "Loading...",
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(spacingCommon),
    )
}

@Preview
@Composable
private fun PreviewLoaderLight() {
    AppThemePreview(darkTheme = false) {
        Loader()
    }
}

@Preview
@Composable
private fun PreviewLoaderDark() {
    AppThemePreview(darkTheme = true) {
        Loader()
    }
}
