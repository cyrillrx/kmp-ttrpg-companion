package com.cyrillrx.rpg.character.domain

import com.cyrillrx.core.domain.truncate
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.jvm.JvmInline

const val MAX_CHARACTER_NAME_LENGTH = 50

@Serializable(with = CharacterNameSerializer::class)
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

private const val BLANK_NAME_PLACEHOLDER = "?"

internal object CharacterNameSerializer : KSerializer<CharacterName> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("CharacterName", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: CharacterName) = encoder.encodeString(value.value)

    override fun deserialize(decoder: Decoder): CharacterName {
        return CharacterName.parse(decoder.decodeString()) ?: CharacterName(BLANK_NAME_PLACEHOLDER)
    }
}
