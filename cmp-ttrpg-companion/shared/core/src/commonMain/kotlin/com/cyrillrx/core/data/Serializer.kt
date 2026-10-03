package com.cyrillrx.core.data

import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

val defaultSerializer: Json = Json {
    explicitNulls = false
    ignoreUnknownKeys = true
    isLenient = true
}

// A user opens an exported file to see what they are about to share.
val exportSerializer: Json = Json(defaultSerializer) {
    prettyPrint = true
}

@Throws(SerializationException::class, IllegalArgumentException::class)
inline fun <reified T> String.deserialize(): T = defaultSerializer.decodeFromString<T>(this)

@Throws(SerializationException::class)
inline fun <reified T> T.serialize(): String = defaultSerializer.encodeToString(this)
