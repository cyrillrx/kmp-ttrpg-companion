package com.cyrillrx.rpg.userlist.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun EmptyListLayout(
    icon: ImageVector,
    message: String,
    btnText: String,
    onBtnClicked: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(72.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBtnClicked) {
            Text(text = btnText)
        }
    }
}

@Preview
@Composable
private fun PreviewEmptyListLayoutLight() {
    EmptyListLayoutPreview(darkTheme = false)
}

@Preview
@Composable
private fun PreviewEmptyListLayoutDark() {
    EmptyListLayoutPreview(darkTheme = true)
}

@Composable
private fun EmptyListLayoutPreview(darkTheme: Boolean) {
    AppThemePreview(darkTheme = darkTheme) {
        EmptyListLayout(
            icon = Icons.AutoMirrored.Outlined.MenuBook,
            message = "The list is empty",
            btnText = "Browse spells",
            onBtnClicked = {},
        )
    }
}
