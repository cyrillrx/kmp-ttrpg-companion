package com.cyrillrx.rpg.core.domain

import kotlin.time.Instant

/**
 * Pairs a domain entity with the storage-layer timestamp describing when its record
 * was last updated. Keeps this persistence metadata out of the domain entities
 * themselves.
 */
data class Stored<T>(
    val value: T,
    val updatedAt: Instant,
)

/**
 * Timestamp of a record whose real date is unknown, such as a read-only preset or a
 * row written before the timestamps existed. The UI renders no relative date for it.
 */
val UNKNOWN_TIMESTAMP: Instant = Instant.fromEpochMilliseconds(0L)
