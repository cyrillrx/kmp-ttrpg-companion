package com.cyrillrx.core.data

// Clamping keeps the imported record usable, which a Result.Failure would not; the warning is what
// tells the homebrew author their file declares a value the rules cannot hold.
fun <T> T.coerceAndWarn(source: String, id: String, field: String, coerce: (T) -> T): T = coerce(this)
    .also { if (it != this) println("WARNING: $source '$id' $field $this coerced to $it") }
