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
import rpg_companion.composeapp.generated.resources.monster_ac
import rpg_companion.composeapp.generated.resources.monster_hp
import rpg_companion.composeapp.generated.resources.monster_speed

@Composable
fun MainStatLayout(
    monster: Monster,
    modifier: Modifier = Modifier,
) {
    val translation = monster.resolveTranslation(currentLocale())
    Column(modifier) {
        Text(
            buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                    ),
                ) {
                    append(monster.getSubtitle())
                }
            },
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(Res.string.monster_ac))
                    append(" ")
                }
                append(monster.armorClass.toString())
            },
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(Res.string.monster_hp))
                    append(" ")
                }
                append(monster.maxHitPoints.toString())
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
                    append(stringResource(Res.string.monster_speed))
                    append(" ")
                }
                append(translation?.speed ?: monster.speed)
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
