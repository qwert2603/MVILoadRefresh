package com.qwert2603.mvi_load_refresh.util

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ViewAnimator

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View =
        LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)

fun ViewAnimator.showIfNotYet(child: Int) {
    if (child != displayedChild) {
        displayedChild = child
    }
}