package com.cyrillrx.rpg.core.domain

import kotlinx.datetime.Instant

/**
 * Pairs a domain entity with the storage-layer timestamps that describe when its
 * record was created and last updated. Keeps these persistence metadata out of the
 * domain entities themselves.
 */
data class Stored<T>(
    val value: T,
    val createdAt: Instant,
    val updatedAt: Instant,
)
