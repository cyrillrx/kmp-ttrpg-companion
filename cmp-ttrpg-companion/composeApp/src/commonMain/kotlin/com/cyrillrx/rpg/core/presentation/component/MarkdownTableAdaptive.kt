
package com.cyrillrx.rpg.core.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
 * Markdown table renderer with adaptive column widths.
 *
 * Column widths are distributed proportionally to each column's minimum content width
 * (= width of its longest word across all rows). This avoids the equal-weight layout
 * that wastes space on short columns (e.g. "1d8" vs long description text).
 *
 * If the total minimum width exceeds available space, horizontal scroll is activated.
 */
@Composable
fun MarkdownTableAdaptive(
    content: String,
    node: ASTNode,
    style: TextStyle,
) {
    val tableCellPadding = LocalMarkdownDimens.current.tableCellPadding
    val tableCornerSize = LocalMarkdownDimens.current.tableCornerSize
    val backgroundCodeColor = LocalMarkdownColors.current.tableBackground
    val textMeasurer = rememberTextMeasurer()
    val annotatorSettings = annotatorSettings()

    val headerNode = node.findChildOfType(HEADER) ?: return
    val columnCount = headerNode.children.count { it.type == CELL }
    if (columnCount == 0) return

    val dataRows = node.children.filter { it.type == ROW }

    // For each column, find the widest single word across all rows (header included).
    // Cell text is stripped of Markdown syntax before splitting, so that link URLs
    // ([text](url) → text) and emphasis markers (* **) don't create artificially long
    // "words" that skew the proportional width distribution.
    val colMinWordWidths = remember(content, node) {
        (0 until columnCount).map { colIdx ->
            val rows: List<ASTNode> = listOf(headerNode) + dataRows
            rows.maxOf { rowNode ->
                val cell = rowNode.children.filter { it.type == CELL }.getOrNull(colIdx)
                    ?: return@maxOf 1
                val cellText = content.substring(cell.startOffset, cell.endOffset)
                    .trim()
                    .stripMarkdown()
                if (cellText.isEmpty()) 1
                else cellText.split(Regex("\\s+")).maxOf { word ->
                    textMeasurer.measure(word.trim().ifEmpty { " " }, style = style).size.width
                }
            }
        }
    }

    val density = LocalDensity.current

    BoxWithConstraints(
        modifier = Modifier
            .background(backgroundCodeColor, RoundedCornerShape(tableCornerSize))
            .fillMaxWidth(),
    ) {
        val availablePx = constraints.maxWidth
        val paddingPx = with(density) { (tableCellPadding * 2).roundToPx() }
        val totalMinPx = colMinWordWidths.sumOf { it + paddingPx }
        val scrollable = totalMinPx > availablePx

        val colWidths: List<Dp> = if (scrollable) {
            // Each column gets exactly its min-word width + padding
            colMinWordWidths.map { w -> with(density) { (w + paddingPx).toDp() } }
        } else {
            // Distribute available width proportionally to min-word widths
            val totalMin = colMinWordWidths.sum().coerceAtLeast(1)
            colMinWordWidths.map { w ->
                with(density) { ((availablePx.toLong() * w / totalMin).toInt()).toDp() }
            }
        }

        val totalWidthDp = colWidths.fold(0.dp) { acc, dp -> acc + dp }

        Column(
            modifier = if (scrollable) {
                Modifier.horizontalScroll(rememberScrollState()).requiredWidth(totalWidthDp)
            } else {
                Modifier.fillMaxWidth()
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

// Strips Markdown formatting before word-width measurement so that constructs like
// [text](very/long/url) or *emphasis* don't produce artificially wide "words".
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
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
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
