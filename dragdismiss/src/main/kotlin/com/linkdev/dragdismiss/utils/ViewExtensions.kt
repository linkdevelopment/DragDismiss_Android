/**
 * Copyright (c) 2020-present Link Development
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.linkdev.dragdismiss.utils

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

internal fun View.canViewScrollUp(x: Float, y: Float): Boolean {
    return if (!contains(x, y)) false
    else canScrollVertically(1)
}

internal fun View.canViewScrollDown(x: Float, y: Float): Boolean {
    return if (!contains(x, y)) false
    else canScrollVertically(-1)
}

internal fun View.canViewScrollRight(x: Float, y: Float): Boolean {
    return if (!contains(x, y)) false
    else canScrollHorizontally(-1)
}

internal fun View.canViewScrollLeft(x: Float, y: Float): Boolean {
    return if (!contains(x, y)) false
    else canScrollHorizontally(1)
}
