package com.cyrillrx.ktlint

import com.pinterest.ktlint.test.KtLintAssertThat.Companion.assertThatRule
import com.pinterest.ktlint.test.LintViolation
import org.junit.jupiter.api.Test

class BlankLineAfterBlockRuleTest {
    private val assertThatCode = assertThatRule { BlankLineAfterBlockRule() }

    @Test
    fun `adds a blank line after an if block followed by a statement`() {
        val code =
            """
            fun foo(a: Boolean) {
                if (a) {
                    bar()
                }
                baz()
            }
            """.trimIndent()
        val formattedCode =
            """
            fun foo(a: Boolean) {
                if (a) {
                    bar()
                }

                baz()
            }
            """.trimIndent()

        assertThatCode(code)
            .hasLintViolation(line = 4, col = 6, detail = MISSING_BLANK_LINE_MESSAGE)
            .isFormattedAs(formattedCode)
    }

    @Test
    fun `adds a blank line after an if else chain but not inside it`() {
        val code =
            """
            fun foo(a: Boolean) {
                if (a) {
                    bar()
                } else if (!a) {
                    baz()
                } else {
                    qux()
                }
                bar()
            }
            """.trimIndent()
        val formattedCode =
            """
            fun foo(a: Boolean) {
                if (a) {
                    bar()
                } else if (!a) {
                    baz()
                } else {
                    qux()
                }

                bar()
            }
            """.trimIndent()

        assertThatCode(code)
            .hasLintViolation(line = 8, col = 6, detail = MISSING_BLANK_LINE_MESSAGE)
            .isFormattedAs(formattedCode)
    }

    @Test
    fun `adds a blank line after a single line conditional`() {
        val code =
            """
            fun foo(a: Boolean) {
                if (a) bar() else baz()
                qux()
            }
            """.trimIndent()
        val formattedCode =
            """
            fun foo(a: Boolean) {
                if (a) bar() else baz()

                qux()
            }
            """.trimIndent()

        assertThatCode(code)
            .hasLintViolation(line = 2, col = 28, detail = MISSING_BLANK_LINE_MESSAGE)
            .isFormattedAs(formattedCode)
    }

    @Test
    fun `adds a blank line after when try for while and do while statements`() {
        val code =
            """
            fun foo(a: Int) {
                when (a) {
                    1 -> bar()
                }
                try {
                    bar()
                } catch (e: Exception) {
                    baz()
                } finally {
                    qux()
                }
                for (i in 0..a) {
                    bar()
                }
                while (a > 0) {
                    bar()
                }
                do {
                    bar()
                } while (a > 0)
                baz()
            }
            """.trimIndent()
        val formattedCode =
            """
            fun foo(a: Int) {
                when (a) {
                    1 -> bar()
                }

                try {
                    bar()
                } catch (e: Exception) {
                    baz()
                } finally {
                    qux()
                }

                for (i in 0..a) {
                    bar()
                }

                while (a > 0) {
                    bar()
                }

                do {
                    bar()
                } while (a > 0)

                baz()
            }
            """.trimIndent()

        assertThatCode(code)
            .hasLintViolations(
                LintViolation(line = 4, col = 6, detail = MISSING_BLANK_LINE_MESSAGE),
                LintViolation(line = 11, col = 6, detail = MISSING_BLANK_LINE_MESSAGE),
                LintViolation(line = 14, col = 6, detail = MISSING_BLANK_LINE_MESSAGE),
                LintViolation(line = 17, col = 6, detail = MISSING_BLANK_LINE_MESSAGE),
                LintViolation(line = 20, col = 20, detail = MISSING_BLANK_LINE_MESSAGE),
            ).isFormattedAs(formattedCode)
    }

    @Test
    fun `adds a blank line after a multiline lambda statement`() {
        val code =
            """
            fun foo(a: String?) {
                a?.let {
                    bar(it)
                }
                baz()
            }
            """.trimIndent()
        val formattedCode =
            """
            fun foo(a: String?) {
                a?.let {
                    bar(it)
                }

                baz()
            }
            """.trimIndent()

        assertThatCode(code)
            .hasLintViolation(line = 4, col = 6, detail = MISSING_BLANK_LINE_MESSAGE)
            .isFormattedAs(formattedCode)
    }

    @Test
    fun `adds a blank line after a declaration initialized with a block`() {
        val code =
            """
            fun foo(a: Boolean) {
                val b =
                    if (a) {
                        1
                    } else {
                        2
                    }
                bar(b)
            }
            """.trimIndent()
        val formattedCode =
            """
            fun foo(a: Boolean) {
                val b =
                    if (a) {
                        1
                    } else {
                        2
                    }

                bar(b)
            }
            """.trimIndent()

        assertThatCode(code)
            .hasLintViolation(line = 7, col = 10, detail = MISSING_BLANK_LINE_MESSAGE)
            .isFormattedAs(formattedCode)
    }

    @Test
    fun `adds a blank line after a guard clause`() {
        val code =
            """
            fun foo(a: String?) {
                val b = a ?: return
                bar(b)
            }
            """.trimIndent()
        val formattedCode =
            """
            fun foo(a: String?) {
                val b = a ?: return

                bar(b)
            }
            """.trimIndent()

        assertThatCode(code)
            .hasLintViolation(line = 2, col = 24, detail = MISSING_BLANK_LINE_MESSAGE)
            .isFormattedAs(formattedCode)
    }

    @Test
    fun `adds a blank line after a guard clause ending with error`() {
        val code =
            """
            fun foo(a: String?) {
                val b = a ?: error("missing")
                bar(b)
            }
            """.trimIndent()
        val formattedCode =
            """
            fun foo(a: String?) {
                val b = a ?: error("missing")

                bar(b)
            }
            """.trimIndent()

        assertThatCode(code)
            .hasLintViolation(line = 2, col = 34, detail = MISSING_BLANK_LINE_MESSAGE)
            .isFormattedAs(formattedCode)
    }

    @Test
    fun `adds a blank line after a destructuring guard clause`() {
        val code =
            """
            fun foo(a: Pair<Int, Int>?) {
                val (b, c) = a ?: return
                bar(b, c)
            }
            """.trimIndent()
        val formattedCode =
            """
            fun foo(a: Pair<Int, Int>?) {
                val (b, c) = a ?: return

                bar(b, c)
            }
            """.trimIndent()

        assertThatCode(code)
            .hasLintViolation(line = 2, col = 29, detail = MISSING_BLANK_LINE_MESSAGE)
            .isFormattedAs(formattedCode)
    }

    @Test
    fun `keeps consecutive guard clauses together`() {
        val code =
            """
            fun foo(a: String?, b: String?, c: Boolean) {
                if (c) return
                val d = a ?: return
                b ?: throw IllegalStateException()
                for (x in d) {
                    if (x == ' ') continue
                    if (x == '.') {
                        break
                    }
                    bar(x)
                }
            }
            """.trimIndent()
        val formattedCode =
            """
            fun foo(a: String?, b: String?, c: Boolean) {
                if (c) return
                val d = a ?: return
                b ?: throw IllegalStateException()

                for (x in d) {
                    if (x == ' ') continue
                    if (x == '.') {
                        break
                    }

                    bar(x)
                }
            }
            """.trimIndent()

        assertThatCode(code)
            .hasLintViolations(
                LintViolation(line = 4, col = 39, detail = MISSING_BLANK_LINE_MESSAGE),
                LintViolation(line = 9, col = 10, detail = MISSING_BLANK_LINE_MESSAGE),
            ).isFormattedAs(formattedCode)
    }

    @Test
    fun `keeps consecutive single line conditionals together`() {
        val code =
            """
            fun foo(a: Boolean, b: Boolean) = buildList {
                if (a) add("A")
                if (b) add("B") else add("C")
                add("D")
            }
            """.trimIndent()
        val formattedCode =
            """
            fun foo(a: Boolean, b: Boolean) = buildList {
                if (a) add("A")
                if (b) add("B") else add("C")

                add("D")
            }
            """.trimIndent()

        assertThatCode(code)
            .hasLintViolation(line = 3, col = 34, detail = MISSING_BLANK_LINE_MESSAGE)
            .isFormattedAs(formattedCode)
    }

    @Test
    fun `adds the blank line after a trailing comment`() {
        val code =
            """
            fun foo(a: Boolean) {
                if (a) {
                    bar()
                } // done
                baz()
            }
            """.trimIndent()
        val formattedCode =
            """
            fun foo(a: Boolean) {
                if (a) {
                    bar()
                } // done

                baz()
            }
            """.trimIndent()

        assertThatCode(code)
            .hasLintViolation(line = 4, col = 14, detail = MISSING_BLANK_LINE_MESSAGE)
            .isFormattedAs(formattedCode)
    }

    @Test
    fun `adds the blank line before a comment line that follows a block`() {
        val code =
            """
            fun foo(a: Boolean) {
                if (a) {
                    bar()
                }
                // next
                baz()
            }
            """.trimIndent()
        val formattedCode =
            """
            fun foo(a: Boolean) {
                if (a) {
                    bar()
                }

                // next
                baz()
            }
            """.trimIndent()

        assertThatCode(code)
            .hasLintViolation(line = 4, col = 6, detail = MISSING_BLANK_LINE_MESSAGE)
            .isFormattedAs(formattedCode)
    }

    @Test
    fun `adds the blank line after a trailing comment on a declaration`() {
        val code =
            """
            fun foo() {
                val b = run {
                    1
                } // done
                bar(b)
            }
            """.trimIndent()
        val formattedCode =
            """
            fun foo() {
                val b = run {
                    1
                } // done

                bar(b)
            }
            """.trimIndent()

        assertThatCode(code)
            .hasLintViolation(line = 4, col = 14, detail = MISSING_BLANK_LINE_MESSAGE)
            .isFormattedAs(formattedCode)
    }

    @Test
    fun `adds a blank line after a labeled loop`() {
        val code =
            """
            fun foo(a: List<Int>) {
                loop@ for (x in a) {
                    bar(x)
                }
                baz()
            }
            """.trimIndent()
        val formattedCode =
            """
            fun foo(a: List<Int>) {
                loop@ for (x in a) {
                    bar(x)
                }

                baz()
            }
            """.trimIndent()

        assertThatCode(code)
            .hasLintViolation(line = 4, col = 6, detail = MISSING_BLANK_LINE_MESSAGE)
            .isFormattedAs(formattedCode)
    }

    @Test
    fun `adds a blank line after an assignment guard clause`() {
        val code =
            """
            fun foo(a: String?) {
                var b = ""
                b = a ?: return
                bar(b)
            }
            """.trimIndent()
        val formattedCode =
            """
            fun foo(a: String?) {
                var b = ""
                b = a ?: return

                bar(b)
            }
            """.trimIndent()

        assertThatCode(code)
            .hasLintViolation(line = 3, col = 20, detail = MISSING_BLANK_LINE_MESSAGE)
            .isFormattedAs(formattedCode)
    }

    @Test
    fun `adds a blank line inside a when branch block`() {
        val code =
            """
            fun foo(a: Int) {
                when (a) {
                    1 -> {
                        if (a > 0) {
                            bar()
                        }
                        baz()
                    }
                }
            }
            """.trimIndent()
        val formattedCode =
            """
            fun foo(a: Int) {
                when (a) {
                    1 -> {
                        if (a > 0) {
                            bar()
                        }

                        baz()
                    }
                }
            }
            """.trimIndent()

        assertThatCode(code)
            .hasLintViolation(line = 6, col = 14, detail = MISSING_BLANK_LINE_MESSAGE)
            .isFormattedAs(formattedCode)
    }

    @Test
    fun `adds a blank line after a block in a script`() {
        val code =
            """
            plugins {
                id("foo")
            }
            version = "1"
            """.trimIndent()
        val formattedCode =
            """
            plugins {
                id("foo")
            }

            version = "1"
            """.trimIndent()

        assertThatCode(code)
            .asKotlinScript()
            .hasLintViolation(line = 3, col = 2, detail = MISSING_BLANK_LINE_MESSAGE)
            .isFormattedAs(formattedCode)
    }

    @Test
    fun `adds a blank line after a multi-line guard clause`() {
        val code =
            """
            fun foo(a: Boolean, b: Boolean) {
                if (a) {
                    return
                }
                if (b) bar()
                baz()
            }
            """.trimIndent()
        val formattedCode =
            """
            fun foo(a: Boolean, b: Boolean) {
                if (a) {
                    return
                }

                if (b) bar()

                baz()
            }
            """.trimIndent()

        assertThatCode(code)
            .hasLintViolations(
                LintViolation(line = 4, col = 6, detail = MISSING_BLANK_LINE_MESSAGE),
                LintViolation(line = 5, col = 17, detail = MISSING_BLANK_LINE_MESSAGE),
            ).isFormattedAs(formattedCode)
    }

    @Test
    fun `adds the blank line after several trailing comments`() {
        val code =
            """
            fun foo(a: Boolean) {
                if (a) {
                    bar()
                } /* x */ // y
                baz()
            }
            """.trimIndent()
        val formattedCode =
            """
            fun foo(a: Boolean) {
                if (a) {
                    bar()
                } /* x */ // y

                baz()
            }
            """.trimIndent()

        assertThatCode(code)
            .hasLintViolation(line = 4, col = 19, detail = MISSING_BLANK_LINE_MESSAGE)
            .isFormattedAs(formattedCode)
    }

    @Test
    fun `adds a blank line after an annotated statement`() {
        val code =
            """
            fun foo(a: List<Int>) {
                @Suppress("UNUSED") for (x in a) {
                    bar(x)
                }
                baz()
            }
            """.trimIndent()
        val formattedCode =
            """
            fun foo(a: List<Int>) {
                @Suppress("UNUSED") for (x in a) {
                    bar(x)
                }

                baz()
            }
            """.trimIndent()

        assertThatCode(code)
            .hasLintViolation(line = 4, col = 6, detail = MISSING_BLANK_LINE_MESSAGE)
            .isFormattedAs(formattedCode)
    }

    @Test
    fun `adds a blank line after a top level statement of a script`() {
        val code =
            """
            for (x in listOf(1)) println(x)
            println()
            """.trimIndent()
        val formattedCode =
            """
            for (x in listOf(1)) println(x)

            println()
            """.trimIndent()

        assertThatCode(code)
            .asKotlinScript()
            .hasLintViolation(line = 1, col = 32, detail = MISSING_BLANK_LINE_MESSAGE)
            .isFormattedAs(formattedCode)
    }

    @Test
    fun `ignores a block that ends its enclosing block`() {
        val code =
            """
            fun foo(a: Boolean) {
                bar()
                if (a) {
                    bar()
                }
            }
            """.trimIndent()

        assertThatCode(code).hasNoLintViolations()
    }

    @Test
    fun `ignores a block already followed by a blank line`() {
        val code =
            """
            fun foo(a: Boolean) {
                if (a) {
                    bar()
                }

                baz()
            }
            """.trimIndent()

        assertThatCode(code).hasNoLintViolations()
    }

    @Test
    fun `ignores single line statements and multiline calls`() {
        val code =
            """
            fun foo(a: List<Int>) {
                val b = a.map { it + 1 }
                bar(
                    b,
                )
                a.forEach { bar(it) }
                val c =
                    a
                        .sortedBy { it }
                        .map { it * 2 }
                baz(c)
            }
            """.trimIndent()

        assertThatCode(code).hasNoLintViolations()
    }

    @Test
    fun `ignores statements sharing a line`() {
        val code =
            """
            fun foo(a: Boolean) {
                if (a) bar(); baz()
            }
            """.trimIndent()

        assertThatCode(code).hasNoLintViolations()
    }

    @Test
    fun `ignores declarations of a class body`() {
        val code =
            """
            class Foo {
                fun bar() {
                    baz()
                }
                fun qux() = Unit
            }
            """.trimIndent()

        assertThatCode(code).hasNoLintViolations()
    }
}
