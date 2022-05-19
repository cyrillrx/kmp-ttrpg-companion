package com.cyrillrx.rpg

import android.os.Build
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView

private val isNOrAbove = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N

fun TextView.setHtmlText(html: String?) {

    text = when {
        html.isNullOrBlank() -> ""
        isNOrAbove -> Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
        else -> Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
    }
}

fun EditText.onChange(onChange: (String?) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(newText: Editable?) {
            onChange(newText?.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}
