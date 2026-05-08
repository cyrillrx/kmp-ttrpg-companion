
package com.cyrillrx.rpg.core.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
 * Renders a Markdown table at its natural width.
 *
 * Each column is sized to its widest single-line content (header measured bold,
 * data measured regular). If the total fits in the available space the table
 * occupies only what it needs. If the total exceeds the available space all
 * columns are scaled down proportionally so the table still fits without
 * horizontal scroll.
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

    val naturalWidths = rememberNaturalColWidths(content, node, columnCount, style, dimens.tableCellPadding)

    BoxWithConstraints(
        modifier = Modifier.background(backgroundCodeColor, RoundedCornerShape(dimens.tableCornerSize)),
    ) {
        val availableDp = with(density) { constraints.maxWidth.toDp() }
        val totalNaturalDp = naturalWidths.fold(0.dp) { acc, dp -> acc + dp }

        val colWidths = if (totalNaturalDp <= availableDp) {
            naturalWidths
        } else {
            val scale = availableDp.value / totalNaturalDp.value
            naturalWidths.map { it * scale }
        }

        Column(modifier = Modifier.width(colWidths.fold(0.dp) { acc, dp -> acc + dp })) {
            TableContent(content, node, colWidths, style, dimens.tableCellPadding, annotatorSettings)
        }
    }
}

/**
 * Renders a Markdown table that always fills the available width.
 *
 * Natural column widths are computed the same way as [MarkdownTableCompact].
 * All columns are scaled proportionally so their total equals the available
 * width — widening short-content tables and narrowing oversized ones alike.
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

    val naturalWidths = rememberNaturalColWidths(content, node, columnCount, style, dimens.tableCellPadding)

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundCodeColor, RoundedCornerShape(dimens.tableCornerSize)),
    ) {
        val availableDp = with(density) { constraints.maxWidth.toDp() }
        val totalNaturalDp = naturalWidths.fold(0.dp) { acc, dp -> acc + dp }
        val scale = availableDp.value / totalNaturalDp.value
        val colWidths = naturalWidths.map { it * scale }

        Column(modifier = Modifier.fillMaxWidth()) {
            TableContent(content, node, colWidths, style, dimens.tableCellPadding, annotatorSettings)
        }
    }
}

// Computes natural column widths: ideal single-line text width + 2×padding.
// Headers are measured bold, data rows regular — matching how they are rendered.
// Markdown is stripped before measurement so link URLs and emphasis markers
// don't inflate the measured width.
@Composable
private fun rememberNaturalColWidths(
    content: String,
    node: ASTNode,
    columnCount: Int,
    style: TextStyle,
    tableCellPadding: Dp,
): List<Dp> {
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current
    return remember(content, node, density, style, tableCellPadding) {
        val headerNode = node.findChildOfType(HEADER) ?: return@remember emptyList()
        val dataRows = node.children.filter { it.type == ROW }
        val boldStyle = style.copy(fontWeight = FontWeight.Bold)
        val paddingPx = with(density) { (tableCellPadding * 2).roundToPx() }
        (0 until columnCount).map { colIdx ->
            val headerPx = headerNode.children.filter { it.type == CELL }.getOrNull(colIdx)
                ?.let { cell ->
                    val text = content.substring(cell.startOffset, cell.endOffset).trim().stripMarkdown()
                    if (text.isEmpty()) 1
                    else textMeasurer.measure(text, style = boldStyle, softWrap = false).size.width
                } ?: 1
            val dataPx = dataRows.maxOfOrNull { rowNode ->
                val cell = rowNode.children.filter { it.type == CELL }.getOrNull(colIdx)
                    ?: return@maxOfOrNull 1
                val text = content.substring(cell.startOffset, cell.endOffset).trim().stripMarkdown()
                if (text.isEmpty()) 1
                else textMeasurer.measure(text, style = style, softWrap = false).size.width
            } ?: 1
            with(density) { (maxOf(headerPx, dataPx) + paddingPx).toDp() }
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
            val colWidth = colWidths.getOrElse(i) { colWidths.last() }
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
