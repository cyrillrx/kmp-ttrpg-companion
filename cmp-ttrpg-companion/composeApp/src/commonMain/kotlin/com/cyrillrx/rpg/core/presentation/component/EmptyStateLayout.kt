package com.cyrillrx.rpg.core.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun EmptyStateLayout(
    icon: ImageVector,
    message: String,
    modifier: Modifier = Modifier,
    action: @Composable ColumnScope.() -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacingCommon, Alignment.CenterVertically),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(72.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        )
        Text(
            text = message,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = spacingCommon),
        )
        action()
    }
}

@Preview
@Composable
private fun PreviewEmptyStateLayoutLight() {
    EmptyStateLayoutPreview(darkTheme = false)
}

@Preview
@Composable
private fun PreviewEmptyStateLayoutDark() {
    EmptyStateLayoutPreview(darkTheme = true)
}

@Composable
private fun EmptyStateLayoutPreview(darkTheme: Boolean) {
    AppThemePreview(darkTheme = darkTheme) {
        EmptyStateLayout(
            icon = Icons.Outlined.Map,
            message = "No campaign yet",
        )
    }
}
