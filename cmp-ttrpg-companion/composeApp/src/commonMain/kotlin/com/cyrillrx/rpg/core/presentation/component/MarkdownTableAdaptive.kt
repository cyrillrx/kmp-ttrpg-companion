
package com.cyrillrx.rpg.core.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mikepenz.markdown.annotator.annotatorSettings
import com.mikepenz.markdown.compose.LocalMarkdownColors
import com.mikepenz.markdown.compose.LocalMarkdownDimens
import com.mikepenz.markdown.compose.elements.MarkdownDivider
import com.mikepenz.markdown.compose.elements.MarkdownTableBasicText
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.findChildOfType
import org.intellij.markdown.flavours.gfm.GFMElementTypes.HEADER
import org.intellij.markdown.flavours.gfm.GFMElementTypes.ROW
import org.intellij.markdown.flavours.gfm.GFMTokenTypes.CELL
import org.intellij.markdown.flavours.gfm.GFMTokenTypes.TABLE_SEPARATOR

/**
 * Markdown table renderer with natural column widths.
 *
 * Each column is sized to its widest single-line content (Markdown stripped),
 * capped at [tableCellWidth] (160dp by default). The table width equals the sum
 * of column widths — it does not stretch to fill available space.
 *
 * If the total width exceeds the available space, horizontal scroll is activated.
 */
@Composable
fun MarkdownTableAdaptive(
    content: String,
    node: ASTNode,
    style: TextStyle,
) {
    val tableCellPadding = LocalMarkdownDimens.current.tableCellPadding
    val tableCellWidth = LocalMarkdownDimens.current.tableCellWidth
    val tableCornerSize = LocalMarkdownDimens.current.tableCornerSize
    val backgroundCodeColor = LocalMarkdownColors.current.tableBackground
    val textMeasurer = rememberTextMeasurer()
    val annotatorSettings = annotatorSettings()
    val density = LocalDensity.current

    val headerNode = node.findChildOfType(HEADER) ?: return
    val columnCount = headerNode.children.count { it.type == CELL }
    if (columnCount == 0) return

    val dataRows = node.children.filter { it.type == ROW }

    // Each column width = min(single-line ideal text width, tableCellWidth) + padding.
    // Markdown is stripped before measurement so link URLs and emphasis markers
    // don't inflate the measured width.
    val colWidths: List<Dp> = remember(content, node, density, tableCellWidth, tableCellPadding) {
        val capPx = with(density) { tableCellWidth.roundToPx() }
        val paddingPx = with(density) { (tableCellPadding * 2).roundToPx() }
        (0 until columnCount).map { colIdx ->
            val rows: List<ASTNode> = listOf(headerNode) + dataRows
            val idealTextPx = rows.maxOf { rowNode ->
                val cell = rowNode.children.filter { it.type == CELL }.getOrNull(colIdx)
                    ?: return@maxOf 1
                val cellText = content.substring(cell.startOffset, cell.endOffset)
                    .trim()
                    .stripMarkdown()
                if (cellText.isEmpty()) 1
                else textMeasurer.measure(cellText, style = style, softWrap = false).size.width
                    .coerceAtMost(capPx)
            }
            with(density) { (idealTextPx + paddingPx).toDp() }
        }
    }

    BoxWithConstraints(
        modifier = Modifier.background(backgroundCodeColor, RoundedCornerShape(tableCornerSize)),
    ) {
        val availablePx = constraints.maxWidth
        val totalWidthDp = colWidths.fold(0.dp) { acc, dp -> acc + dp }
        val totalWidthPx = with(density) { totalWidthDp.roundToPx() }
        val scrollable = totalWidthPx > availablePx

        Column(
            modifier = if (scrollable) {
                Modifier.horizontalScroll(rememberScrollState()).requiredWidth(totalWidthDp)
            } else {
                Modifier.width(totalWidthDp)
            },
        ) {
            node.children.forEach { child ->
                when (child.type) {
                    HEADER -> AdaptiveTableRow(
                        content = content,
                        rowNode = child,
                        colWidths = colWidths,
                        style = style.copy(fontWeight = FontWeight.Bold),
                        cellPadding = tableCellPadding,
                        annotatorSettings = annotatorSettings,
                    )
                    ROW -> AdaptiveTableRow(
                        content = content,
                        rowNode = child,
                        colWidths = colWidths,
                        style = style,
                        cellPadding = tableCellPadding,
                        annotatorSettings = annotatorSettings,
                    )
                    TABLE_SEPARATOR -> MarkdownDivider()
                }
            }
        }
    }
}

// Strips Markdown formatting before single-line width measurement so that constructs
// like [text](very/long/url) or *emphasis* don't produce artificially wide "words".
private fun String.stripMarkdown(): String = this
    .replace(Regex("\\[([^]]+)]\\([^)]*\\)"), "$1") // [text](url) → text
    .replace(Regex("[*_`]+"), "")                    // *, **, _, __, ` → nothing

@Composable
private fun AdaptiveTableRow(
    content: String,
    rowNode: ASTNode,
    colWidths: List<Dp>,
    style: TextStyle,
    cellPadding: Dp,
    annotatorSettings: com.mikepenz.markdown.annotator.AnnotatorSettings,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(IntrinsicSize.Min),
    ) {
        rowNode.children.filter { it.type == CELL }.forEachIndexed { i, cell ->
            val colWidth = colWidths.getOrElse(i) { colWidths.lastOrNull() ?: 80.dp }
            Column(
                modifier = Modifier
                    .width(colWidth)
                    .padding(cellPadding),
            ) {
                MarkdownTableBasicText(
                    content = content,
                    cell = cell,
                    style = style,
                    maxLines = Int.MAX_VALUE,
                    overflow = TextOverflow.Clip,
                    annotatorSettings = annotatorSettings,
                )
            }
        }
    }
}
