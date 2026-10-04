package com.cyrillrx.rpg.character.presentation

import com.cyrillrx.rpg.character.data.CharacterFileImportError
import com.cyrillrx.rpg.character.data.CharacterImportError
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.error_import_malformed_file
import rpg_companion.composeapp.generated.resources.error_import_missing_field
import rpg_companion.composeapp.generated.resources.error_import_not_json
import rpg_companion.composeapp.generated.resources.error_import_unknown_value
import rpg_companion.composeapp.generated.resources.error_import_unsupported_file
import rpg_companion.composeapp.generated.resources.label_armor_class
import rpg_companion.composeapp.generated.resources.label_description
import rpg_companion.composeapp.generated.resources.label_languages
import rpg_companion.composeapp.generated.resources.label_race
import rpg_companion.composeapp.generated.resources.label_short_description
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CharacterImportFailureTest {

    @Test
    fun `a file that is not JSON says so`() {
        assertEquals(
            CharacterImportFailure(Res.string.error_import_not_json),
            CharacterFileImportError.InvalidJson.toImportFailure(),
        )
    }

    @Test
    fun `a malformed envelope has its own message`() {
        assertEquals(
            CharacterImportFailure(Res.string.error_import_malformed_file),
            CharacterFileImportError.MalformedEnvelope.toImportFailure(),
        )
    }

    @Test
    fun `a newer format and another entity type both ask for an update`() {
        val expected = CharacterImportFailure(Res.string.error_import_unsupported_file)

        assertEquals(expected, CharacterFileImportError.UnsupportedFormatVersion(2).toImportFailure())
        assertEquals(expected, CharacterFileImportError.UnsupportedEntityType("spell").toImportFailure())
    }

    @Test
    fun `a missing field is named`() {
        val error = CharacterFileImportError.InvalidCharacter(CharacterImportError.MissingArmorClass(ID))

        assertEquals(
            CharacterImportFailure(Res.string.error_import_missing_field, Res.string.label_armor_class),
            error.toImportFailure(),
        )
    }

    @Test
    fun `every missing field gets its own label`() {
        val errors = listOf(
            CharacterImportError.MissingId,
            CharacterImportError.MissingName(ID),
            CharacterImportError.MissingRace(ID),
            CharacterImportError.MissingClasses(ID),
            CharacterImportError.MissingSize(ID),
            CharacterImportError.MissingAlignment(ID),
            CharacterImportError.MissingArmorClass(ID),
            CharacterImportError.MissingMaxHitPoints(ID),
            CharacterImportError.MissingSkills(ID),
            CharacterImportError.MissingWalkSpeed(ID),
            CharacterImportError.MissingTranslations(ID),
        )

        val failures = errors.map { CharacterFileImportError.InvalidCharacter(it).toImportFailure() }

        assertTrue(failures.all { it.message == Res.string.error_import_missing_field })
        assertEquals(errors.size, failures.mapNotNull { it.field }.distinct().size)
    }

    @Test
    fun `every unknown value names its field and keeps the value`() {
        val errors = listOf(
            CharacterImportError.UnknownRace(ID, VALUE),
            CharacterImportError.UnknownClass(ID, VALUE),
            CharacterImportError.UnknownSize(ID, VALUE),
            CharacterImportError.UnknownAlignment(ID, VALUE),
            CharacterImportError.UnknownLanguages(ID, listOf(VALUE)),
        )

        val failures = errors.map { CharacterFileImportError.InvalidCharacter(it).toImportFailure() }

        assertTrue(failures.all { it.message == Res.string.error_import_unknown_value && it.value == VALUE })
        assertEquals(errors.size, failures.mapNotNull { it.field }.distinct().size)
    }

    @Test
    fun `an unknown value is named with the value the file declared`() {
        val error = CharacterFileImportError.InvalidCharacter(CharacterImportError.UnknownRace(ID, "kobold"))

        assertEquals(
            CharacterImportFailure(Res.string.error_import_unknown_value, Res.string.label_race, "kobold"),
            error.toImportFailure(),
        )
    }

    @Test
    fun `every unknown language is listed`() {
        val error = CharacterFileImportError.InvalidCharacter(
            CharacterImportError.UnknownLanguages(ID, listOf("klingon", "sindarin")),
        )

        assertEquals(
            CharacterImportFailure(
                Res.string.error_import_unknown_value,
                Res.string.label_languages,
                "klingon, sindarin",
            ),
            error.toImportFailure(),
        )
    }

    @Test
    fun `an invalid translation names the description field it lacks`() {
        val shortDescription = CharacterImportError.InvalidTranslation(ID, "fr", field = "shortDescription")
        val description = CharacterImportError.InvalidTranslation(ID, "fr", field = "description")

        assertEquals(
            Res.string.label_short_description,
            CharacterFileImportError.InvalidCharacter(shortDescription).toImportFailure().field,
        )
        assertEquals(
            Res.string.label_description,
            CharacterFileImportError.InvalidCharacter(description).toImportFailure().field,
        )
    }

    private companion object {
        const val ID = "imported"
        const val VALUE = "homebrew"
    }
}
