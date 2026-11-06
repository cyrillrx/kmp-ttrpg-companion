package com.cyrillrx.ktlint

import com.pinterest.ktlint.rule.engine.core.api.AutocorrectDecision
import com.pinterest.ktlint.rule.engine.core.api.ElementType
import com.pinterest.ktlint.rule.engine.core.api.Rule
import com.pinterest.ktlint.rule.engine.core.api.RuleAutocorrectApproveHandler
import com.pinterest.ktlint.rule.engine.core.api.RuleId
import com.pinterest.ktlint.rule.engine.core.api.ifAutocorrectAllowed
import com.pinterest.ktlint.rule.engine.core.api.isCodeLeaf
import com.pinterest.ktlint.rule.engine.core.api.isWhiteSpaceWithNewline
import com.pinterest.ktlint.rule.engine.core.api.isWhiteSpaceWithoutNewline
import com.pinterest.ktlint.rule.engine.core.api.lastChildLeafOrSelf
import com.pinterest.ktlint.rule.engine.core.api.prevCodeLeaf
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.psi.PsiComment
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.LeafPsiElement
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.KtBinaryExpression
import org.jetbrains.kotlin.psi.KtBlockExpression
import org.jetbrains.kotlin.psi.KtBreakExpression
import org.jetbrains.kotlin.psi.KtContinueExpression
import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.kotlin.psi.KtIfExpression
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.KtPsiUtil
import org.jetbrains.kotlin.psi.KtReturnExpression
import org.jetbrains.kotlin.psi.KtThrowExpression

internal const val MISSING_BLANK_LINE_MESSAGE = "Missing blank line after a block statement"

/**
 * Requires a blank line between a block statement and the statement that follows it in the same block.
 *
 * A block statement is a control-flow statement (`if`, `when`, `try`, `for`, `while`, `do`), a guard clause
 * (`val x = y ?: return`, `if (x) return`) or any statement ending with a multiline block, such as `x?.let { }`.
 * Consecutive guard clauses and single-line conditionals form a group and may stay together.
 */
class BlankLineAfterBlockRule :
    Rule(
        ruleId = RuleId("$RULE_SET_ID:blank-line-after-block"),
        about =
            About(
                maintainer = "cyrillrx",
                repositoryUrl = "https://github.com/cyrillrx/ttrpg-companion",
                issueTrackerUrl = "https://github.com/cyrillrx/ttrpg-companion/issues",
            ),
    ),
    RuleAutocorrectApproveHandler {
    override fun beforeVisitChildNodes(
        node: ASTNode,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> AutocorrectDecision,
    ) {
        if (node.elementType != ElementType.BLOCK) return

        node
            .getChildren(null)
            .filter { it.psi is KtExpression }
            .zipWithNext()
            .forEach { (statement, next) -> visitPair(statement, next, emit) }
    }

    private fun visitPair(
        statement: ASTNode,
        next: ASTNode,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> AutocorrectDecision,
    ) {
        val body = statement.withoutLabelOrAnnotation()
        if (!body.isBlockStatement()) return
        if (body.isCompact() && next.withoutLabelOrAnnotation().isCompact()) return

        val separator = separatorAfter(statement) ?: return
        if (separator.text.count { it == '\n' } != 1) return

        emit(separator.startOffset, MISSING_BLANK_LINE_MESSAGE, true)
            .ifAutocorrectAllowed { (separator as LeafPsiElement).rawReplaceWithText("\n" + separator.text) }
    }

    /**
     * The whitespace that ends the statement's line, after its trailing comment if any, or `null` when another
     * statement shares that line.
     */
    private fun separatorAfter(statement: ASTNode): ASTNode? {
        var current = statement.treeNext
        if (current.isWhiteSpaceWithoutNewline() && current.treeNext.psi is PsiComment) current = current.treeNext
        if (current.psi is PsiComment) current = current.treeNext

        return current.takeIf { it.isWhiteSpaceWithNewline() }
    }

    private fun ASTNode.withoutLabelOrAnnotation(): ASTNode =
        KtPsiUtil.deparenthesize(psi as KtExpression)?.node ?: this

    private fun ASTNode.isBlockStatement(): Boolean =
        elementType in CONTROL_FLOW_STATEMENTS ||
            isGuard() ||
            endsWithMultilineBlock()

    private fun ASTNode.endsWithMultilineBlock(): Boolean {
        val lastLeaf = lastChildLeafOrSelf().let { if (it.isCodeLeaf()) it else it.prevCodeLeaf() }
        return lastLeaf?.elementType == ElementType.RBRACE && lastLeaf.treeParent.textContains('\n')
    }

    private fun ASTNode.isCompact(): Boolean = isGuard() || (elementType == ElementType.IF && !textContains('\n'))

    private fun ASTNode.isGuard(): Boolean =
        when (val psi = psi) {
            is KtProperty -> psi.initializer.isElvisJump()
            is KtBinaryExpression -> psi.isElvisJump() || psi.isAssignedFromElvisJump()
            is KtIfExpression -> psi.`else` == null && psi.then.isJumpBranch()
            else -> false
        }

    private fun KtBinaryExpression.isAssignedFromElvisJump(): Boolean =
        operationToken == KtTokens.EQ && right.isElvisJump()

    private fun KtExpression?.isElvisJump(): Boolean =
        this is KtBinaryExpression && operationToken == KtTokens.ELVIS && right.isJump()

    private fun KtExpression?.isJumpBranch(): Boolean =
        isJump() || (this is KtBlockExpression && statements.singleOrNull().isJump())

    private fun KtExpression?.isJump(): Boolean =
        this is KtReturnExpression ||
            this is KtThrowExpression ||
            this is KtBreakExpression ||
            this is KtContinueExpression

    private companion object {
        val CONTROL_FLOW_STATEMENTS =
            setOf(
                ElementType.IF,
                ElementType.WHEN,
                ElementType.TRY,
                ElementType.FOR,
                ElementType.WHILE,
                ElementType.DO_WHILE,
            )
    }
}
