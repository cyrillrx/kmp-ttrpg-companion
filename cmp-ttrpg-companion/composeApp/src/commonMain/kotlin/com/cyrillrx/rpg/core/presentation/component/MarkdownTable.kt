
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
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
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.mikepenz.markdown.annotator.annotatorSettings
import com.mikepenz.markdown.compose.LocalMarkdownColors
import com.mikepenz.markdown.compose.LocalMarkdownDimens
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.compose.elements.MarkdownDivider
import com.mikepenz.markdown.compose.elements.MarkdownTableBasicText
import com.mikepenz.markdown.m3.Markdown
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.findChildOfType
import org.intellij.markdown.flavours.gfm.GFMElementTypes.HEADER
import org.intellij.markdown.flavours.gfm.GFMElementTypes.ROW
import org.intellij.markdown.flavours.gfm.GFMTokenTypes.CELL
import org.intellij.markdown.flavours.gfm.GFMTokenTypes.TABLE_SEPARATOR
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Renders a Markdown table at its minimum natural width.
 *
 * Each column is sized to its widest single-line content (Markdown stripped),
 * capped at [LocalMarkdownDimens.tableCellWidth] (160dp by default). The table
 * is only as wide as the sum of its columns — it does not stretch to fill
 * available space. Horizontal scroll activates when the total exceeds the
 * available width.
 */
@Composable
fun MarkdownTableCompact(
    content: String,
    node: ASTNode,
    style: TextStyle,
) {
    val dimens = LocalMarkdownDimens.current
    val backgroundCodeColor = LocalMarkdownColors.current.tableBackground
    val annotatorSettings = annotatorSettings()
    val density = LocalDensity.current

    val headerNode = node.findChildOfType(HEADER) ?: return
    val columnCount = headerNode.children.count { it.type == CELL }
    if (columnCount == 0) return

    val colWidths = rememberNaturalColWidths(content, node, columnCount, style, dimens.tableCellWidth, dimens.tableCellPadding)

    BoxWithConstraints(
        modifier = Modifier.background(backgroundCodeColor, RoundedCornerShape(dimens.tableCornerSize)),
    ) {
        val totalWidthDp = colWidths.fold(0.dp) { acc, dp -> acc + dp }
        val scrollable = with(density) { totalWidthDp.roundToPx() } > constraints.maxWidth

        Column(
            modifier = if (scrollable) {
                Modifier.horizontalScroll(rememberScrollState()).requiredWidth(totalWidthDp)
            } else {
                Modifier.width(totalWidthDp)
            },
        ) {
            TableContent(content, node, colWidths, style, dimens.tableCellPadding, annotatorSettings)
        }
    }
}

/**
 * Renders a Markdown table that fills the available width.
 *
 * Natural column widths are computed the same way as [MarkdownTableCompact].
 * When the total natural width is smaller than the available space, the surplus
 * is distributed proportionally to each column's natural width so the table
 * always fills the container. Horizontal scroll activates only when the natural
 * total exceeds the available width.
 */
@Composable
fun MarkdownTableFillWidth(
    content: String,
    node: ASTNode,
    style: TextStyle,
) {
    val dimens = LocalMarkdownDimens.current
    val backgroundCodeColor = LocalMarkdownColors.current.tableBackground
    val annotatorSettings = annotatorSettings()
    val density = LocalDensity.current

    val headerNode = node.findChildOfType(HEADER) ?: return
    val columnCount = headerNode.children.count { it.type == CELL }
    if (columnCount == 0) return

    val naturalWidths = rememberNaturalColWidths(content, node, columnCount, style, dimens.tableCellWidth, dimens.tableCellPadding)

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundCodeColor, RoundedCornerShape(dimens.tableCornerSize)),
    ) {
        val availableDp = with(density) { constraints.maxWidth.toDp() }
        val totalNaturalDp = naturalWidths.fold(0.dp) { acc, dp -> acc + dp }
        val scrollable = totalNaturalDp > availableDp

        val colWidths = if (scrollable) {
            naturalWidths
        } else {
            val surplusDp = availableDp - totalNaturalDp
            val totalNaturalFloat = naturalWidths.fold(0f) { acc, dp -> acc + dp.value }
            naturalWidths.map { w -> w + surplusDp * (w.value / totalNaturalFloat) }
        }

        Column(
            modifier = if (scrollable) {
                Modifier.horizontalScroll(rememberScrollState()).requiredWidth(totalNaturalDp)
            } else {
                Modifier.fillMaxWidth()
            },
        ) {
            TableContent(content, node, colWidths, style, dimens.tableCellPadding, annotatorSettings)
        }
    }
}

// Computes natural column widths: min(ideal single-line text width, cap) + 2×padding.
// Markdown is stripped before measurement so link URLs and emphasis markers
// don't inflate the measured width.
@Composable
private fun rememberNaturalColWidths(
    content: String,
    node: ASTNode,
    columnCount: Int,
    style: TextStyle,
    tableCellWidth: Dp,
    tableCellPadding: Dp,
): List<Dp> {
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current
    return remember(content, node, density, tableCellWidth, tableCellPadding) {
        val headerNode = node.findChildOfType(HEADER) ?: return@remember emptyList()
        val dataRows = node.children.filter { it.type == ROW }
        val capPx = with(density) { tableCellWidth.roundToPx() }
        val paddingPx = with(density) { (tableCellPadding * 2).roundToPx() }
        val allRows: List<ASTNode> = listOf(headerNode) + dataRows
        (0 until columnCount).map { colIdx ->
            val idealTextPx = allRows.maxOf { rowNode ->
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
}

private fun String.stripMarkdown(): String = this
    .replace(Regex("\\[([^]]+)]\\([^)]*\\)"), "$1") // [text](url) → text
    .replace(Regex("[*_`]+"), "")                    // *, **, _, __, ` → nothing

@Composable
private fun TableContent(
    content: String,
    node: ASTNode,
    colWidths: List<Dp>,
    style: TextStyle,
    cellPadding: Dp,
    annotatorSettings: com.mikepenz.markdown.annotator.AnnotatorSettings,
) {
    node.children.forEach { child ->
        when (child.type) {
            HEADER -> TableRow(content, child, colWidths, style.copy(fontWeight = FontWeight.Bold), cellPadding, annotatorSettings)
            ROW -> TableRow(content, child, colWidths, style, cellPadding, annotatorSettings)
            TABLE_SEPARATOR -> MarkdownDivider()
        }
    }
}

@Composable
private fun TableRow(
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

private val SAMPLE_TABLE = """
    | Spell Level | Rarity | Spell Save DC | Attack Bonus |
    |-------------|--------|---------------|--------------|
    | 0 (cantrip) | Common | 13 | +5 |
    | 1st–2nd | Common | 13 | +5 |
    | 3rd–4th | Uncommon | 15 | +7 |
    | 5th–6th | Rare | 17 | +9 |
    | 7th–8th | Very Rare | 18 | +10 |
""".trimIndent()

@Preview
@Composable
private fun PreviewMarkdownTableComparisonLight() {
    PreviewMarkdownTableComparison(darkTheme = false)
}

@Preview
@Composable
private fun PreviewMarkdownTableComparisonDark() {
    PreviewMarkdownTableComparison(darkTheme = true)
}

@Composable
private fun PreviewMarkdownTableComparison(darkTheme: Boolean) {
    val compactComponents = markdownComponents(
        table = { m -> MarkdownTableCompact(m.content, m.node, m.typography.table) },
    )
    val fillWidthComponents = markdownComponents(
        table = { m -> MarkdownTableFillWidth(m.content, m.node, m.typography.table) },
    )
    AppThemePreview(darkTheme = darkTheme) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
        ) {
            Text("MarkdownTableCompact — minimum width")
            Markdown(content = SAMPLE_TABLE, modifier = Modifier.fillMaxWidth(), components = compactComponents)
            Text("MarkdownTableFillWidth — fills available width")
            Markdown(content = SAMPLE_TABLE, modifier = Modifier.fillMaxWidth(), components = fillWidthComponents)
        }
    }
}
