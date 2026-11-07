package com.cyrillrx.core.data

data class Imported<out T, out W>(val value: T, val warnings: List<W>)
