package com.cyrillrx.rpg

import android.os.Build
import android.text.Html
import android.widget.TextView

/**
 * @author Cyril Leroux
 *         Created on 12/03/2020.
 */


private val isNOrAbove = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N

fun TextView.setHtmlText(html: String?) {

    text = when {
        html.isNullOrBlank() -> ""
        isNOrAbove -> Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
        else -> Html.fromHtml(html)
    }
}
