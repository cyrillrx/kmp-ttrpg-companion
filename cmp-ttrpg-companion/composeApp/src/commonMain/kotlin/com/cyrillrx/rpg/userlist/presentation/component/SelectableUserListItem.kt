package com.cyrillrx.rpg.userlist.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.userlist.data.SampleUserListRepository
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SelectableUserListItem(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(onClick = onClick, modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacingCommon),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f),
            )
            if (isSelected) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.Circle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewSelectableUserListItemLight() {
    SelectableUserListItemPreview(false, false)
}

@Preview
@Composable
private fun PreviewSelectableUserListItemDark() {
    SelectableUserListItemPreview(true, true)
}

@Composable
private fun SelectableUserListItemPreview(darkTheme: Boolean, isSelected: Boolean) {
    AppThemePreview(darkTheme = darkTheme) {
        SelectableUserListItem(
            name = SampleUserListRepository.getFirst().name,
            isSelected = isSelected,
            onClick = {},
        )
    }
}
