package com.cyrillrx.rpg.xml

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View =
    LayoutInflater
        .from(this.context)
        .inflate(layoutRes, this, attachToRoot)
