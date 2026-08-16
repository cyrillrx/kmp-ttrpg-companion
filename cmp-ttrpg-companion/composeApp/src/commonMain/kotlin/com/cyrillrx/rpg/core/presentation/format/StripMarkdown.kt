package com.cyrillrx.rpg.core.presentation.format

private val LINK_REGEX = Regex("\\[([^]]+)]\\([^)]*\\)")
private val MARKDOWN_CHARS_REGEX = Regex("[*_`]+")

internal fun String.stripMarkdown(): String = this
    .replace(LINK_REGEX, "$1") // [text](url) → text
    .replace(MARKDOWN_CHARS_REGEX, "") // *, **, _, __, ` → nothing
