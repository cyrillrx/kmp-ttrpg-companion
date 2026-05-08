package com.cyrillrx.rpg.core.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.m3.Markdown

private val markdownComponents = markdownComponents(
    table = { model ->
        MarkdownTableAdaptive(
            content = model.content,
            node = model.node,
            style = model.typography.table,
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
