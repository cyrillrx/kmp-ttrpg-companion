package com.cyrillrx.ktlint

import com.pinterest.ktlint.rule.engine.core.api.AutocorrectDecision
import com.pinterest.ktlint.rule.engine.core.api.ElementType
import com.pinterest.ktlint.rule.engine.core.api.Rule
import com.pinterest.ktlint.rule.engine.core.api.RuleAutocorrectApproveHandler
import com.pinterest.ktlint.rule.engine.core.api.RuleId
import com.pinterest.ktlint.rule.engine.core.api.ifAutocorrectAllowed
import com.pinterest.ktlint.rule.engine.core.api.isCode
import com.pinterest.ktlint.rule.engine.core.api.isWhiteSpaceWithNewline20
import com.pinterest.ktlint.rule.engine.core.api.isWhiteSpaceWithoutNewline20
import com.pinterest.ktlint.rule.engine.core.api.lastChildLeafOrSelf20
import com.pinterest.ktlint.rule.engine.core.api.prevCodeLeaf
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.psi.PsiComment
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.LeafPsiElement
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.KtBinaryExpression
import org.jetbrains.kotlin.psi.KtBlockExpression
import org.jetbrains.kotlin.psi.KtBreakExpression
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtContinueExpression
import org.jetbrains.kotlin.psi.KtDestructuringDeclaration
import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.kotlin.psi.KtIfExpression
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.KtPsiUtil
import org.jetbrains.kotlin.psi.KtReturnExpression
import org.jetbrains.kotlin.psi.KtScriptInitializer
import org.jetbrains.kotlin.psi.KtThrowExpression

internal const val MISSING_BLANK_LINE_MESSAGE = "Missing blank line after a block statement"

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
        val body = statement.bareStatement()
        if (!body.isBlockStatement()) return
        if (body.isCompact() && !body.endsWithMultilineBlock() && next.bareStatement().isCompact()) return

        val separator = separatorAfter(statement) ?: return
        if (separator.text.count { it == '\n' } != 1) return

        emit(separator.startOffset, MISSING_BLANK_LINE_MESSAGE, true)
            .ifAutocorrectAllowed { (separator as LeafPsiElement).rawReplaceWithText("\n" + separator.text) }
    }

    private fun separatorAfter(statement: ASTNode): ASTNode? {
        val lineEnd =
            generateSequence(statement.treeNext) { it.treeNext }
                .first { !it.isWhiteSpaceWithoutNewline20 && it.psi !is PsiComment }

        return lineEnd.takeIf { it.isWhiteSpaceWithNewline20 }
    }

    private fun ASTNode.bareStatement(): ASTNode {
        val expression = (psi as? KtScriptInitializer)?.body ?: psi as KtExpression
        return KtPsiUtil.deparenthesize(expression)?.node ?: this
    }

    private fun ASTNode.isBlockStatement(): Boolean =
        elementType in CONTROL_FLOW_STATEMENTS ||
            isGuard() ||
            endsWithMultilineBlock()

    private fun ASTNode.endsWithMultilineBlock(): Boolean {
        val lastLeaf = lastChildLeafOrSelf20.let { if (it.isCode) it else it.prevCodeLeaf }
        return lastLeaf?.elementType == ElementType.RBRACE && lastLeaf.treeParent.textContains('\n')
    }

    private fun ASTNode.isCompact(): Boolean = isGuard() || (elementType == ElementType.IF && !textContains('\n'))

    private fun ASTNode.isGuard(): Boolean =
        when (val psi = psi) {
            is KtProperty -> psi.initializer.isElvisJump()
            is KtDestructuringDeclaration -> psi.initializer.isElvisJump()
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
            this is KtContinueExpression ||
            (this is KtCallExpression && calleeExpression?.text in NOTHING_FUNCTIONS)

    private companion object {
        val NOTHING_FUNCTIONS = setOf("error", "TODO")

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
