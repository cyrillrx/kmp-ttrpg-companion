package com.cyrillrx.rpg.usercollection.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.core.presentation.component.AppCard
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.usercollection.data.SampleUserCollectionRepository
import com.cyrillrx.rpg.usercollection.domain.UserCollection
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Instant

@Composable
fun UserCollectionItem(
    collection: UserCollection,
    updatedAt: Instant,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AppCard(onClick = onClick, modifier = modifier) {
        Column(modifier = Modifier.padding(spacingCommon)) {
            Text(
                text = collection.name,
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = collection.subtitle(updatedAt),
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewUserCollectionItemLight() {
    UserCollectionItemPreview(false)
}

@Preview
@Composable
private fun PreviewUserCollectionItemDark() {
    UserCollectionItemPreview(true)
}

@Composable
private fun UserCollectionItemPreview(darkTheme: Boolean) {
    AppThemePreview(darkTheme = darkTheme) {
        val sample = SampleUserCollectionRepository.getFirst()
        UserCollectionItem(
            collection = sample.value,
            updatedAt = sample.updatedAt,
            onClick = {},
        )
    }
}
