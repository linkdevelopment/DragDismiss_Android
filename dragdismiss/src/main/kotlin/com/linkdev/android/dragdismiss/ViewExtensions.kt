package com.linkdev.android.dragdismiss

import android.graphics.Rect
import android.view.View
import android.webkit.WebView
import android.widget.AbsListView
import android.widget.HorizontalScrollView
import android.widget.ScrollView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager


internal fun View.contains(x: Float, y: Float): Boolean {
    val localRect = Rect()
    this.getGlobalVisibleRect(localRect)
    return localRect.contains(x.toInt(), y.toInt())
}

internal fun View.isScrollableView(): Boolean {
    return this is ScrollView
            || this is HorizontalScrollView
            || this is NestedScrollView
            || this is AbsListView
            || this is RecyclerView
            || this is ViewPager
            || this is WebView
}
