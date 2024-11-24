package com.cyrillrx.rpg.xml

import android.view.View
import androidx.annotation.DrawableRes

/**
 * @author Cyril Leroux
 *          Created on 17/07/2018.
 */
class PlaceholderConfig private constructor(
        val drawableRes: Int? = null,
        val title: String? = null,
        val subtitle: String? = null,
        val actionLabel: String? = null,
        val onClick: ((v: View) -> Unit)? = null) {

    class Builder {

        private var drawableRes: Int? = null
        private var title: String? = null
        private var subtitle: String? = null
        private var actionLabel: String? = null
        private var onClick: ((v: View) -> Unit)? = null

        fun setDrawableRes(@DrawableRes drawableRes: Int): Builder {
            this.drawableRes = drawableRes
            return this
        }

        fun setTitle(title: String?): Builder {
            this.title = title
            return this
        }

        fun setSubtitle(subtitle: String?): Builder {
            this.subtitle = subtitle
            return this
        }

        fun setAction(label: String?, onClick: ((v: View) -> Unit)? = null): Builder {
            this.actionLabel = label
            this.onClick = onClick
            return this
        }

        fun build(): PlaceholderConfig =
                PlaceholderConfig(drawableRes, title, subtitle, actionLabel, onClick)
    }

    companion object {

        fun blank() = Builder().build()
    }
}
