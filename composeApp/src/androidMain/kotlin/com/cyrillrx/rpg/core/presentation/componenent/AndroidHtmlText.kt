package com.cyrillrx.rpg.core.presentation.componenent

import android.graphics.Typeface
import android.os.Build.VERSION.SDK_INT
import android.text.Html
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.core.text.getSpans

const val ANNOTATION_TAG_URL = "url"

/**
 * Simple Text composable to show the text with html styling from a String.
 * Supported are:
 *
 * <b>Bold</b>
 * <i>Italic</i>
 * <u>Underlined</u>
 * <strike>Strikethrough</strike>
 * <a href="https://google.de">Link</a>
 */
@Composable
fun AndroidHtmlText(
    modifier: Modifier = Modifier,
    text: String,
    urlSpanStyle: SpanStyle = SpanStyle(
        color = MaterialTheme.colorScheme.tertiary,
        textDecoration = TextDecoration.Underline,
    ),
    colorMapping: Map<Color, Color> = emptyMap(),
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    inlineContent: Map<String, InlineTextContent> = mapOf(),
    onTextLayout: ((TextLayoutResult) -> Unit)? = {},
    style: TextStyle = LocalTextStyle.current,
    onUriClick: ((String) -> Unit)? = null,
) {
    val annotatedString = if (SDK_INT < 24) {
        Html.fromHtml(text)
    } else {
        Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
    }.toAnnotatedString(urlSpanStyle, colorMapping)

    AndroidHtmlText(
        modifier = modifier,
        annotatedString = annotatedString,
        color = color,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        inlineContent = inlineContent,
        onTextLayout = onTextLayout,
        style = style,
        onUriClick = onUriClick,
    )
}

/**
 * Simple Text composable to show the text with html styling from a String.
 * Supported are:
 *
 * <b>Bold</b>
 * <i>Italic>/i>
 * <u>Underlined</u>
 * <strike>Strikethrough</strike>
 * <a href="https://google.de">Link</a>
 *
 * @see androidx.compose.material.Text
 */
@Composable
fun AndroidHtmlText(
    modifier: Modifier = Modifier,
    annotatedString: AnnotatedString,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    inlineContent: Map<String, InlineTextContent> = mapOf(),
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    style: TextStyle = LocalTextStyle.current,
    onUriClick: ((String) -> Unit)? = null,
) {
    val uriHandler = LocalUriHandler.current
    val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }

    val urls = remember(layoutResult, annotatedString) {
        annotatedString.getStringAnnotations(ANNOTATION_TAG_URL, 0, annotatedString.lastIndex)
    }

    val clickable = urls.isNotEmpty()

    Text(
        modifier = modifier.then(
            if (clickable) Modifier
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { pos ->
                            layoutResult.value?.let { layoutResult ->
                                val position = layoutResult.getOffsetForPosition(pos)
                                annotatedString
                                    .getStringAnnotations(position, position)
                                    .firstOrNull()
                                    ?.let { sa ->
                                        if (sa.tag == ANNOTATION_TAG_URL) { // NON-NLS
                                            val url = sa.item
                                            onUriClick?.let { it(url) } ?: uriHandler.openUri(url)
                                        }
                                    }
                            }
                        },
                    )
                }
                .semantics {
                    if (urls.size == 1) {
                        role = Role.Button
                        onClick("Link (${annotatedString.substring(urls[0].start, urls[0].end)}") {
                            val url = urls[0].item
                            onUriClick?.let { it(url) } ?: uriHandler.openUri(url)
                            true
                        }
                    } else {
                        customActions = urls.map {
                            CustomAccessibilityAction("Link (${annotatedString.substring(it.start, it.end)})") {
                                val url = it.item
                                onUriClick?.let { it(url) } ?: uriHandler.openUri(url)
                                true
                            }
                        }
                    }
                } else Modifier,
        ),
        text = annotatedString,
        color = color,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        inlineContent = inlineContent,
        onTextLayout = {
            layoutResult.value = it
            onTextLayout?.invoke(it)
        },
        style = style,
    )
}

fun Spanned.toAnnotatedString(
    urlSpanStyle: SpanStyle = SpanStyle(
        color = Color.Blue,
        textDecoration = TextDecoration.Underline,
    ),
    colorMapping: Map<Color, Color> = emptyMap(),
): AnnotatedString {
    return buildAnnotatedString {
        append(this@toAnnotatedString.toString())
        val urlSpans = getSpans<URLSpan>()
        urlSpans.forEach { urlSpan ->
            val start = getSpanStart(urlSpan)
            val end = getSpanEnd(urlSpan)
            addStyle(urlSpanStyle, start, end)
            addStringAnnotation(ANNOTATION_TAG_URL, urlSpan.url, start, end) // NON-NLS
        }

        val colorSpans = getSpans<ForegroundColorSpan>()
        colorSpans.forEach { colorSpan ->
            val start = getSpanStart(colorSpan)
            val end = getSpanEnd(colorSpan)
            addStyle(
                SpanStyle(color = colorMapping.getOrElse(Color(colorSpan.foregroundColor)) { Color(colorSpan.foregroundColor) }),
                start,
                end,
            )
        }

        val styleSpans = getSpans<StyleSpan>()
        styleSpans.forEach { styleSpan ->
            val start = getSpanStart(styleSpan)
            val end = getSpanEnd(styleSpan)
            when (styleSpan.style) {
                Typeface.BOLD -> addStyle(SpanStyle(fontWeight = FontWeight.Bold), start, end)
                Typeface.ITALIC -> addStyle(SpanStyle(fontStyle = FontStyle.Italic), start, end)
                Typeface.BOLD_ITALIC -> addStyle(
                    SpanStyle(fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic),
                    start,
                    end,
                )
            }
        }

        val underlineSpans = getSpans<UnderlineSpan>()
        underlineSpans.forEach { underlineSpan ->
            val start = getSpanStart(underlineSpan)
            val end = getSpanEnd(underlineSpan)
            addStyle(SpanStyle(textDecoration = TextDecoration.Underline), start, end)
        }

        val strikethroughSpans = getSpans<StrikethroughSpan>()
        strikethroughSpans.forEach { strikethroughSpan ->
            val start = getSpanStart(strikethroughSpan)
            val end = getSpanEnd(strikethroughSpan)
            addStyle(SpanStyle(textDecoration = TextDecoration.LineThrough), start, end)
        }
    }
}
