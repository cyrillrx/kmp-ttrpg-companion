package com.cyrillrx.rpg.usercollection.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.cyrillrx.rpg.usercollection.data.SampleUserCollectionRepository
import com.cyrillrx.rpg.usercollection.domain.UserCollection
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Instant

@Composable
fun SelectableUserCollectionItem(
    list: UserCollection,
    updatedAt: Instant,
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
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = list.name,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = list.subtitle(updatedAt),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
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
private fun PreviewSelectableUserCollectionItemLight() {
    SelectableUserCollectionItemPreview(false, false)
}

@Preview
@Composable
private fun PreviewSelectableUserCollectionItemDark() {
    SelectableUserCollectionItemPreview(true, true)
}

@Composable
private fun SelectableUserCollectionItemPreview(darkTheme: Boolean, isSelected: Boolean) {
    AppThemePreview(darkTheme = darkTheme) {
        val sample = SampleUserCollectionRepository.getFirst()
        SelectableUserCollectionItem(
            list = sample.value,
            updatedAt = sample.updatedAt,
            isSelected = isSelected,
            onClick = {},
        )
    }
}
