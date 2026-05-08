package com.cyrillrx.rpg.core.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.compose.elements.MarkdownTable
import com.mikepenz.markdown.compose.elements.MarkdownTableHeader
import com.mikepenz.markdown.compose.elements.MarkdownTableRow
import com.mikepenz.markdown.m3.Markdown

private val markdownComponents = markdownComponents(
    table = { model ->
        MarkdownTable(
            content = model.content,
            node = model.node,
            style = model.typography.table,
            headerBlock = { content, header, tableWidth, style ->
                MarkdownTableHeader(
                    content = content,
                    header = header,
                    tableWidth = tableWidth,
                    style = style,
                    maxLines = Int.MAX_VALUE,
                    overflow = TextOverflow.Clip,
                )
            },
            rowBlock = { content, row, tableWidth, style ->
                MarkdownTableRow(
                    content = content,
                    header = row,
                    tableWidth = tableWidth,
                    style = style,
                    maxLines = Int.MAX_VALUE,
                    overflow = TextOverflow.Clip,
                )
            },
        )
    },
)

@Composable
fun MarkdownText(
    text: String,
    modifier: Modifier = Modifier,
) {
    Markdown(
        content = text,
        modifier = modifier,
        components = markdownComponents,
    )
}
