package com.cyrillrx.rpg.home.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HomeButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(text = text)
    }
}

@Composable
fun HomeButtonPreview() {
    HomeButton(text = "Campaigns", onClick = {})
}

@Preview
@Composable
private fun PreviewHomeButtonLight() {
    AppThemePreview(darkTheme = false) {
        HomeButtonPreview()
    }
}

@Preview
@Composable
private fun PreviewHomeButtonDark() {
    AppThemePreview(darkTheme = true) {
        HomeButtonPreview()
    }
}
