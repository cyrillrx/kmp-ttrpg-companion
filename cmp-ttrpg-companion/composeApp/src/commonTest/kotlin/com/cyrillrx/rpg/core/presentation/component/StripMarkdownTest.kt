package com.cyrillrx.rpg.core.presentation.component

import kotlin.test.Test
import kotlin.test.assertEquals

class StripMarkdownTest {

    @Test
    fun `removes bold markers`() {
        assertEquals("bold", "**bold**".stripMarkdown())
    }

    @Test
    fun `removes italic markers`() {
        assertEquals("italic", "*italic*".stripMarkdown())
    }

    @Test
    fun `removes inline code markers`() {
        assertEquals("code", "`code`".stripMarkdown())
    }

    @Test
    fun `replaces link with its display text`() {
        assertEquals("click here", "[click here](https://example.com)".stripMarkdown())
    }

    @Test
    fun `strips emphasis inside a link`() {
        assertEquals("bold link", "[**bold link**](https://example.com)".stripMarkdown())
    }

    @Test
    fun `plain text is unchanged`() {
        assertEquals("1d8", "1d8".stripMarkdown())
    }
}
