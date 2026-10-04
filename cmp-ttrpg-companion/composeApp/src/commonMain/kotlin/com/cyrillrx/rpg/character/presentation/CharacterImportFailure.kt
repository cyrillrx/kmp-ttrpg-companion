package com.cyrillrx.rpg.character.presentation

import com.cyrillrx.rpg.character.data.CharacterFileImportError
import com.cyrillrx.rpg.character.data.CharacterImportError
import org.jetbrains.compose.resources.StringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.error_import_failed
import rpg_companion.composeapp.generated.resources.error_import_malformed_file
import rpg_companion.composeapp.generated.resources.error_import_missing_field
import rpg_companion.composeapp.generated.resources.error_import_not_json
import rpg_companion.composeapp.generated.resources.error_import_unknown_value
import rpg_companion.composeapp.generated.resources.error_import_unsupported_file
import rpg_companion.composeapp.generated.resources.label_alignment
import rpg_companion.composeapp.generated.resources.label_armor_class
import rpg_companion.composeapp.generated.resources.label_class
import rpg_companion.composeapp.generated.resources.label_classes
import rpg_companion.composeapp.generated.resources.label_description
import rpg_companion.composeapp.generated.resources.label_hit_points
import rpg_companion.composeapp.generated.resources.label_identifier
import rpg_companion.composeapp.generated.resources.label_languages
import rpg_companion.composeapp.generated.resources.label_name
import rpg_companion.composeapp.generated.resources.label_race
import rpg_companion.composeapp.generated.resources.label_short_description
import rpg_companion.composeapp.generated.resources.label_size
import rpg_companion.composeapp.generated.resources.label_skills
import rpg_companion.composeapp.generated.resources.label_speed

private const val SHORT_DESCRIPTION_FIELD = "shortDescription"
private const val LANGUAGE_SEPARATOR = ", "

data class CharacterImportFailure(
    val message: StringResource,
    val field: StringResource? = null,
    val value: String? = null,
) {
    companion object {
        val Unexpected = CharacterImportFailure(Res.string.error_import_failed)
    }
}

fun CharacterFileImportError.toImportFailure(): CharacterImportFailure = when (this) {
    CharacterFileImportError.InvalidJson -> CharacterImportFailure(Res.string.error_import_not_json)
    CharacterFileImportError.MalformedEnvelope -> CharacterImportFailure(Res.string.error_import_malformed_file)
    is CharacterFileImportError.UnsupportedFormatVersion,
    is CharacterFileImportError.UnsupportedEntityType,
    -> CharacterImportFailure(Res.string.error_import_unsupported_file)
    is CharacterFileImportError.InvalidCharacter -> cause.toImportFailure()
}

private fun CharacterImportError.toImportFailure(): CharacterImportFailure = when (this) {
    CharacterImportError.MissingId -> missing(Res.string.label_identifier)
    is CharacterImportError.MissingName -> missing(Res.string.label_name)
    is CharacterImportError.MissingRace -> missing(Res.string.label_race)
    is CharacterImportError.MissingClasses -> missing(Res.string.label_classes)
    is CharacterImportError.MissingSize -> missing(Res.string.label_size)
    is CharacterImportError.MissingAlignment -> missing(Res.string.label_alignment)
    is CharacterImportError.MissingArmorClass -> missing(Res.string.label_armor_class)
    is CharacterImportError.MissingMaxHitPoints -> missing(Res.string.label_hit_points)
    is CharacterImportError.MissingSkills -> missing(Res.string.label_skills)
    is CharacterImportError.MissingWalkSpeed -> missing(Res.string.label_speed)
    is CharacterImportError.MissingTranslations -> missing(Res.string.label_description)
    is CharacterImportError.InvalidTranslation -> missing(translationLabel(field))
    is CharacterImportError.UnknownRace -> unknown(Res.string.label_race, value)
    is CharacterImportError.UnknownClass -> unknown(Res.string.label_class, value)
    is CharacterImportError.UnknownSize -> unknown(Res.string.label_size, value)
    is CharacterImportError.UnknownAlignment -> unknown(Res.string.label_alignment, value)
    is CharacterImportError.UnknownLanguages -> unknown(
        Res.string.label_languages,
        values.joinToString(LANGUAGE_SEPARATOR),
    )
}

private fun translationLabel(field: String): StringResource =
    if (field == SHORT_DESCRIPTION_FIELD) Res.string.label_short_description else Res.string.label_description

private fun missing(field: StringResource) = CharacterImportFailure(Res.string.error_import_missing_field, field)

private fun unknown(field: StringResource, value: String) =
    CharacterImportFailure(Res.string.error_import_unknown_value, field, value)
