package com.cyrillrx.rpg.xml

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.cyrillrx.rpg.R

/**
 * @author Cyril Leroux
 *          Created on 07/07/2018.
 */
class PlaceholderLayout : FrameLayout {

    private lateinit var ivImage: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvSubtitle: TextView
    private lateinit var btnAction: Button

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes,
    ) {
        init(context)
    }

    private fun init(context: Context) {
        inflate(context, R.layout.layout_empty, this)

        ivImage = findViewById(R.id.iv_image)
        tvTitle = findViewById(R.id.tv_title)
        tvSubtitle = findViewById(R.id.tv_subtitle)
        btnAction = findViewById(R.id.btn_action)
    }

    fun setup(config: PlaceholderConfig) {
        setImageRes(config.drawableRes)
        setTitle(config.title)
        setSubtitle(config.subtitle)
        setAction(config.actionLabel, config.onClick)
    }

    private fun setImageRes(drawableRes: Int?) {
        if (drawableRes == null) {
            ivImage.visibility = View.GONE
        } else {
            ivImage.visibility = View.VISIBLE
            ivImage.setImageResource(drawableRes)
        }
    }

    private fun setTitle(title: String?) {
        if (title.isNullOrBlank()) {
            tvTitle.visibility = View.GONE
        } else {
            tvTitle.visibility = View.VISIBLE
            tvTitle.text = title
        }
    }

    private fun setSubtitle(subtitle: String?) {
        if (subtitle.isNullOrBlank()) {
            tvSubtitle.visibility = View.GONE
        } else {
            tvSubtitle.visibility = View.VISIBLE
            tvSubtitle.text = subtitle
        }
    }

    private fun setAction(label: String?, onClick: ((v: View) -> Unit)? = null) {
        if (label.isNullOrBlank() || onClick == null) {
            btnAction.visibility = View.GONE
        } else {
            btnAction.visibility = View.VISIBLE
            btnAction.text = label
            btnAction.setOnClickListener(onClick)
        }
    }
}
