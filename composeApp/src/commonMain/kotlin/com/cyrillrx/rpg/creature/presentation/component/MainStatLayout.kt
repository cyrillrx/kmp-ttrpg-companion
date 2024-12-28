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
import com.cyrillrx.rpg.creature.domain.Creature
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.creature_ac
import rpg_companion.composeapp.generated.resources.creature_alignment_chaotic_evil
import rpg_companion.composeapp.generated.resources.creature_alignment_chaotic_good
import rpg_companion.composeapp.generated.resources.creature_alignment_chaotic_neutral
import rpg_companion.composeapp.generated.resources.creature_alignment_lawful_evil
import rpg_companion.composeapp.generated.resources.creature_alignment_lawful_good
import rpg_companion.composeapp.generated.resources.creature_alignment_lawful_neutral
import rpg_companion.composeapp.generated.resources.creature_alignment_neutral
import rpg_companion.composeapp.generated.resources.creature_alignment_neutral_evil
import rpg_companion.composeapp.generated.resources.creature_alignment_neutral_good
import rpg_companion.composeapp.generated.resources.creature_alignment_unknown
import rpg_companion.composeapp.generated.resources.creature_hp
import rpg_companion.composeapp.generated.resources.creature_size_gargantuan
import rpg_companion.composeapp.generated.resources.creature_size_huge
import rpg_companion.composeapp.generated.resources.creature_size_large
import rpg_companion.composeapp.generated.resources.creature_size_medium
import rpg_companion.composeapp.generated.resources.creature_size_small
import rpg_companion.composeapp.generated.resources.creature_size_tiny
import rpg_companion.composeapp.generated.resources.creature_size_unknown
import rpg_companion.composeapp.generated.resources.creature_speed
import rpg_companion.composeapp.generated.resources.creature_subtitle
import rpg_companion.composeapp.generated.resources.creature_type_aberration
import rpg_companion.composeapp.generated.resources.creature_type_beast
import rpg_companion.composeapp.generated.resources.creature_type_celestial
import rpg_companion.composeapp.generated.resources.creature_type_construct
import rpg_companion.composeapp.generated.resources.creature_type_dragon
import rpg_companion.composeapp.generated.resources.creature_type_elemental
import rpg_companion.composeapp.generated.resources.creature_type_fey
import rpg_companion.composeapp.generated.resources.creature_type_fiend
import rpg_companion.composeapp.generated.resources.creature_type_giant
import rpg_companion.composeapp.generated.resources.creature_type_humanoid
import rpg_companion.composeapp.generated.resources.creature_type_monstrosity
import rpg_companion.composeapp.generated.resources.creature_type_ooze
import rpg_companion.composeapp.generated.resources.creature_type_plant
import rpg_companion.composeapp.generated.resources.creature_type_undead
import rpg_companion.composeapp.generated.resources.creature_type_unknown

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

@Composable
private fun Creature.getSubtitle(): String {
    return stringResource(Res.string.creature_subtitle, getFormattedType(), getFormattedSize(), getAlignment())
}

@Composable
private fun Creature.getFormattedType(): String {
    return when (type) {
        Creature.Type.ABERRATION -> stringResource(Res.string.creature_type_aberration)
        Creature.Type.BEAST -> stringResource(Res.string.creature_type_beast)
        Creature.Type.CELESTIAL -> stringResource(Res.string.creature_type_celestial)
        Creature.Type.CONSTRUCT -> stringResource(Res.string.creature_type_construct)
        Creature.Type.DRAGON -> stringResource(Res.string.creature_type_dragon)
        Creature.Type.ELEMENTAL -> stringResource(Res.string.creature_type_elemental)
        Creature.Type.FEY -> stringResource(Res.string.creature_type_fey)
        Creature.Type.FIEND -> stringResource(Res.string.creature_type_fiend)
        Creature.Type.GIANT -> stringResource(Res.string.creature_type_giant)
        Creature.Type.HUMANOID -> stringResource(Res.string.creature_type_humanoid)
        Creature.Type.MONSTROSITY -> stringResource(Res.string.creature_type_monstrosity)
        Creature.Type.OOZE -> stringResource(Res.string.creature_type_ooze)
        Creature.Type.PLANT -> stringResource(Res.string.creature_type_plant)
        Creature.Type.UNDEAD -> stringResource(Res.string.creature_type_undead)
        Creature.Type.UNKNOWN -> stringResource(Res.string.creature_type_unknown)
    }
}

@Composable
private fun Creature.getFormattedSize(): String {
    return when (size) {
        Creature.Size.TINY -> stringResource(Res.string.creature_size_tiny)
        Creature.Size.SMALL -> stringResource(Res.string.creature_size_small)
        Creature.Size.MEDIUM -> stringResource(Res.string.creature_size_medium)
        Creature.Size.LARGE -> stringResource(Res.string.creature_size_large)
        Creature.Size.HUGE -> stringResource(Res.string.creature_size_huge)
        Creature.Size.GARGANTUAN -> stringResource(Res.string.creature_size_gargantuan)
        Creature.Size.UNKNOWN -> stringResource(Res.string.creature_size_unknown)
    }
}

@Composable
private fun Creature.getAlignment(): String {
    return when (alignment) {
        Creature.Alignment.LAWFUL_GOOD -> stringResource(Res.string.creature_alignment_lawful_good)
        Creature.Alignment.NEUTRAL_GOOD -> stringResource(Res.string.creature_alignment_neutral_good)
        Creature.Alignment.CHAOTIC_GOOD -> stringResource(Res.string.creature_alignment_chaotic_good)
        Creature.Alignment.LAWFUL_NEUTRAL -> stringResource(Res.string.creature_alignment_lawful_neutral)
        Creature.Alignment.NEUTRAL -> stringResource(Res.string.creature_alignment_neutral)
        Creature.Alignment.CHAOTIC_NEUTRAL -> stringResource(Res.string.creature_alignment_chaotic_neutral)
        Creature.Alignment.LAWFUL_EVIL -> stringResource(Res.string.creature_alignment_lawful_evil)
        Creature.Alignment.NEUTRAL_EVIL -> stringResource(Res.string.creature_alignment_neutral_evil)
        Creature.Alignment.CHAOTIC_EVIL -> stringResource(Res.string.creature_alignment_chaotic_evil)
        Creature.Alignment.UNKNOWN -> stringResource(Res.string.creature_alignment_unknown)
    }
}
