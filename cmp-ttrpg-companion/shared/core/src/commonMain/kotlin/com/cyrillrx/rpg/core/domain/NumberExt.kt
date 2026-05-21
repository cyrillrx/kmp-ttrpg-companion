package com.cyrillrx.rpg.core.domain

fun Int.toSignedString(): String = if (this >= 0) "+$this" else "$this"
