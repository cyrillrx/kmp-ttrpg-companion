package com.cyrillrx.rpg.creature.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.cyrillrx.rpg.core.presentation.LocalDistanceUnit
import com.cyrillrx.rpg.core.presentation.component.dnd.toFormattedString
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.creature.data.SampleMonsterRepository
import com.cyrillrx.rpg.creature.domain.Monster
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.label_armor_class
import rpg_companion.composeapp.generated.resources.label_hit_points
import rpg_companion.composeapp.generated.resources.label_speed

@Composable
fun MainStatLayout(
    monster: Monster,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Text(
            buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(Res.string.label_armor_class))
                    append(" ")
                }
                append(monster.armorClass.toString())
            },
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(Res.string.label_hit_points))
                    append(" ")
                }
                append(monster.maxHitPoints.toString())
            },
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(Res.string.label_speed))
                    append(" ")
                }
                append(monster.speeds.toFormattedString(LocalDistanceUnit.current))
            },
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Preview
@Composable
private fun PreviewMainStatLayoutLight() {
    AppThemePreview(darkTheme = false) {
        MainStatLayout(monster = SampleMonsterRepository.getFirst())
    }
}

@Preview
@Composable
private fun PreviewMainStatLayoutDark() {
    AppThemePreview(darkTheme = true) {
        MainStatLayout(monster = SampleMonsterRepository.getFirst())
    }
}
