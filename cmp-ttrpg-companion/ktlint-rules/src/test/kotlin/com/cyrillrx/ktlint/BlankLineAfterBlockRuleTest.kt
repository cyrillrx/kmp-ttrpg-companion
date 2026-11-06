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
            .hasLintViolation(4, 6, "Missing blank line after a block statement")
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
            .hasLintViolation(8, 6, "Missing blank line after a block statement")
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
            .hasLintViolation(2, 28, "Missing blank line after a block statement")
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
                LintViolation(4, 6, "Missing blank line after a block statement"),
                LintViolation(11, 6, "Missing blank line after a block statement"),
                LintViolation(14, 6, "Missing blank line after a block statement"),
                LintViolation(17, 6, "Missing blank line after a block statement"),
                LintViolation(20, 20, "Missing blank line after a block statement"),
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
            .hasLintViolation(4, 6, "Missing blank line after a block statement")
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
            .hasLintViolation(7, 10, "Missing blank line after a block statement")
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
            .hasLintViolation(2, 24, "Missing blank line after a block statement")
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
                LintViolation(4, 39, "Missing blank line after a block statement"),
                LintViolation(9, 10, "Missing blank line after a block statement"),
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
            .hasLintViolation(3, 34, "Missing blank line after a block statement")
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
            .hasLintViolation(4, 14, "Missing blank line after a block statement")
            .isFormattedAs(formattedCode)
    }

    @Test
    fun `ignores a block followed by a comment line`() {
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

        assertThatCode(code).hasNoLintViolations()
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
