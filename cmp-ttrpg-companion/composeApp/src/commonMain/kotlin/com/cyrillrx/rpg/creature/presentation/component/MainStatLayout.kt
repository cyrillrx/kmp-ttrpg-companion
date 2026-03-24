package com.cyrillrx.rpg.creature.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.cyrillrx.rpg.core.presentation.component.dnd.getSubtitle
import com.cyrillrx.rpg.creature.domain.Creature
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.creature_ac
import rpg_companion.composeapp.generated.resources.creature_hp
import rpg_companion.composeapp.generated.resources.creature_speed

@Composable
fun MainStatLayout(
    creature: Creature,
    modifier: Modifier = Modifier,
) {
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
        )
        Text(
            buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(Res.string.creature_ac))
                    append(" ")
                }
                append(creature.armorClass.toString())
            },
        )
        Text(
            buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(Res.string.creature_hp))
                    append(" ")
                }
                append(creature.maxHitPoints.toString())
            },
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
                append(creature.speed)
            },
        )
    }
}
