package com.cyrillrx.rpg.character.domain

import com.cyrillrx.core.domain.truncate
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

const val MAX_CHARACTER_NAME_LENGTH = 50

@Serializable
@JvmInline
value class CharacterName(val value: String) {

    init {
        require(value.isNotEmpty() && value.length <= MAX_CHARACTER_NAME_LENGTH && value == value.trim()) {
            "Invalid character name: \"$value\""
        }
    }

    override fun toString(): String = value

    companion object {
        fun parse(raw: String): CharacterName? = raw.trim()
            .truncate(MAX_CHARACTER_NAME_LENGTH)
            .trimEnd()
            .ifEmpty { null }
            ?.let(::CharacterName)
    }
}
