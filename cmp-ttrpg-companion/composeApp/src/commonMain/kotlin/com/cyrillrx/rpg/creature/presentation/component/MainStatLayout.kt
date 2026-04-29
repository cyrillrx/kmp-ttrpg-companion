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
import androidx.compose.ui.unit.sp
import com.cyrillrx.rpg.app.currentLocale
import com.cyrillrx.rpg.core.presentation.component.dnd.getSubtitle
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.creature.data.SampleMonsterRepository
import com.cyrillrx.rpg.creature.domain.Monster
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.creature_ac
import rpg_companion.composeapp.generated.resources.creature_hp
import rpg_companion.composeapp.generated.resources.creature_speed

@Composable
fun MainStatLayout(
    creature: Monster,
    modifier: Modifier = Modifier,
) {
    val translation = creature.resolveTranslation(currentLocale())
    Column(modifier) {
        Text(
            buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                    ),
                ) {
                    append(creature.getSubtitle())
                }
            },
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(Res.string.creature_ac))
                    append(" ")
                }
                append(creature.armorClass.toString())
            },
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(Res.string.creature_hp))
                    append(" ")
                }
                append(creature.maxHitPoints.toString())
            },
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        fontWeight = FontWeight.Bold,
                    ),
                ) {
                    append(stringResource(Res.string.creature_speed))
                    append(" ")
                }
                append(translation?.speed ?: creature.speed)
            },
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Preview
@Composable
private fun PreviewMainStatLayoutLight() {
    AppThemePreview(darkTheme = false) {
        MainStatLayout(creature = SampleMonsterRepository.getFirst())
    }
}

@Preview
@Composable
private fun PreviewMainStatLayoutDark() {
    AppThemePreview(darkTheme = true) {
        MainStatLayout(creature = SampleMonsterRepository.getFirst())
    }
}
