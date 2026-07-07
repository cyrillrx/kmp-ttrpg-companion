package com.cyrillrx.rpg.creature.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.cyrillrx.rpg.app.currentLocale
import com.cyrillrx.rpg.core.presentation.component.AppCard
import com.cyrillrx.rpg.core.presentation.component.dnd.getColor
import com.cyrillrx.rpg.core.presentation.component.dnd.toFormattedCR
import com.cyrillrx.rpg.core.presentation.component.dnd.toFormattedString
import com.cyrillrx.rpg.core.presentation.component.dnd.toIcon
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.iconSizeMediumLarge
import com.cyrillrx.rpg.core.presentation.theme.iconSizeSmall
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import com.cyrillrx.rpg.creature.data.SampleMonsterRepository
import com.cyrillrx.rpg.creature.domain.Monster
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.value_armor_class
import rpg_companion.composeapp.generated.resources.value_hit_points

private val typeIconSize = iconSizeMediumLarge
private val typeIconPadding = spacingCommon

@Composable
fun MonsterListItem(
    monster: Monster,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val translation = monster.resolveTranslation(currentLocale())
    val monsterType = monster.getDisplayType()
    val typeColor = monsterType.getColor()

    AppCard(onClick = onClick, modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(spacingCommon),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(typeIconSize + typeIconPadding * 2)
                    .background(
                        color = typeColor.copy(alpha = 0.12f),
                        shape = MaterialTheme.shapes.small,
                    ),
            ) {
                Icon(
                    imageVector = monsterType.toIcon(),
                    contentDescription = null,
                    tint = typeColor,
                    modifier = Modifier.size(typeIconSize),
                )
            }

            Spacer(modifier = Modifier.width(spacingMedium))

            Column(
                verticalArrangement = Arrangement.spacedBy(spacingSmall),
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = translation.name,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                val formattedMonsterType = monster.types.toFormattedString()
                val formattedMonsterSize = monster.size.toFormattedString()
                Text(
                    text = "$formattedMonsterType · $formattedMonsterSize",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(spacingCommon),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(spacingSmall),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Shield,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(iconSizeSmall),
                        )
                        Text(
                            text = stringResource(Res.string.value_armor_class, monster.armorClass),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(spacingSmall),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Favorite,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(iconSizeSmall),
                        )
                        Text(
                            text = stringResource(Res.string.value_hit_points, monster.maxHitPoints),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(spacingMedium))

            Text(
                text = monster.toFormattedCR(),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = typeColor,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewMonsterListItemLight() {
    AppThemePreview(darkTheme = false) {
        Column(verticalArrangement = Arrangement.spacedBy(spacingSmall)) {
            SampleMonsterRepository.getAll().forEach {
                MonsterListItem(monster = it, onClick = {})
            }
        }
    }
}

@Preview
@Composable
private fun PreviewMonsterListItemDark() {
    AppThemePreview(darkTheme = true) {
        Column(verticalArrangement = Arrangement.spacedBy(spacingSmall)) {
            SampleMonsterRepository.getAll().forEach {
                MonsterListItem(monster = it, onClick = {})
            }
        }
    }
}
