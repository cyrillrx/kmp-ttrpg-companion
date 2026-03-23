package com.cyrillrx.rpg.core.domain

fun <T> Set<T>.toggled(item: T): Set<T> = if (item in this) this - item else this + item
