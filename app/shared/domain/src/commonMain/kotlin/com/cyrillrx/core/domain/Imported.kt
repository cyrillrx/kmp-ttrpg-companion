package com.cyrillrx.core.domain

data class Imported<out T, out W>(val value: T, val warnings: List<W>)
