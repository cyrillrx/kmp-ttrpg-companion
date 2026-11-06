package com.cyrillrx.rpg.usercollection.presentation.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.cyrillrx.rpg.core.presentation.component.EmptyStateLayout
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun EmptyCollectionLayout(
    icon: ImageVector,
    message: String,
    btnText: String,
    onBtnClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    EmptyStateLayout(icon = icon, message = message, modifier = modifier) {
        Button(onClick = onBtnClicked) {
            Text(text = btnText)
        }
    }
}

@Preview
@Composable
private fun PreviewEmptyCollectionLayoutLight() {
    EmptyCollectionLayoutPreview(darkTheme = false)
}

@Preview
@Composable
private fun PreviewEmptyCollectionLayoutDark() {
    EmptyCollectionLayoutPreview(darkTheme = true)
}

@Composable
private fun EmptyCollectionLayoutPreview(darkTheme: Boolean) {
    AppThemePreview(darkTheme = darkTheme) {
        EmptyCollectionLayout(
            icon = Icons.AutoMirrored.Outlined.MenuBook,
            message = "The collection is empty",
            btnText = "Browse spells",
            onBtnClicked = {},
        )
    }
}
