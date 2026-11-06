package com.cyrillrx.rpg.character.data.api

import kotlinx.serialization.Serializable

internal const val CHARACTER_EXPORT_FORMAT_VERSION = 1
internal const val ENTITY_TYPE_CHARACTER = "character"

@Serializable
internal class ApiCharacterExport(
    val formatVersion: Int?,
    val entityType: String?,
    val appVersion: String?,
    val exportedAt: String?,
    val sourceId: String?,
    val character: ApiCharacter?,
)
