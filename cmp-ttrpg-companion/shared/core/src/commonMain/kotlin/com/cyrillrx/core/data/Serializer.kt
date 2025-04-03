package com.cyrillrx.core.data

import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val defaultSerializer: Json = Json {
    explicitNulls = false
    ignoreUnknownKeys = true
    isLenient = true
}

@Throws(SerializationException::class, IllegalArgumentException::class)
inline fun <reified T> String.deserialize(): T = defaultSerializer.decodeFromString<T>(this)

@Throws(SerializationException::class)
inline fun <reified T> T.serialize(): String = defaultSerializer.encodeToString(this)
