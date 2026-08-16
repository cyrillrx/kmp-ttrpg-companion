package com.cyrillrx.rpg.character.domain

import kotlinx.serialization.Serializable

/** A single class a character has levels in. Characters can hold several (multiclassing). */
@Serializable
data class ClassLevel(
    val clazz: Character.Class,
    val level: Int,
)
