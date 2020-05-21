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

package com.linkdev.android.dragdismiss

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import kotlin.math.roundToInt

/**
 * Created by Mohammed.Fareed on 5/22/2018.
 */
internal object Utilities {

    fun extractDirectionsFromFlag(directionFlag: Int): ArrayList<Int> {
        val directions = intArrayOf(
            DragDismissLayout.DragDirections.DIRECTION_FROM_LEFT,
            DragDismissLayout.DragDirections.DIRECTION_FROM_BOTTOM,
            DragDismissLayout.DragDirections.DIRECTION_FROM_RIGHT,
            DragDismissLayout.DragDirections.DIRECTION_FROM_TOP,
            DragDismissLayout.DragDirections.DIRECTION_ALL
        )
        val result = ArrayList<Int>()
        for (direction in directions) {
            if (containsFlag(directionFlag, direction))
                result.add(direction)
        }
        return result
    }

    fun canViewScrollUp(view: View, x: Float, y: Float): Boolean {
        return if (!view.contains(x, y)) false
        else view.canScrollVertically(1)
    }

    fun canViewScrollDown(view: View, x: Float, y: Float): Boolean {
        return if (!view.contains(x, y)) false
        else view.canScrollVertically(-1)
    }

    fun canViewScrollRight(view: View, x: Float, y: Float): Boolean {
        return if (!view.contains(x, y)) false
        else view.canScrollHorizontally(-1)
    }

    fun canViewScrollLeft(view: View, x: Float, y: Float): Boolean {
        return if (!view.contains(x, y)) false
        else view.canScrollHorizontally(1)
    }

    fun findAllScrollViews(viewGroup: ViewGroup): ArrayList<View> {
        val innerScrollViewsList = arrayListOf<View>()

        findAllScrollViews(viewGroup, innerScrollViewsList)
        return innerScrollViewsList
    }

    private fun findAllScrollViews(viewGroup: ViewGroup, scrollViewsList: ArrayList<View>) {
        for (view in viewGroup.children) {
            if (view.visibility != View.VISIBLE) continue

            if (view.isScrollableView())
                scrollViewsList.add(view)

            if (view is ViewGroup)
                findAllScrollViews(view, scrollViewsList)
        }
    }

    private fun containsFlag(flagSet: Int, flag: Int): Boolean {
        return flagSet or flag == flagSet
    }

    fun calculateAlphaFromFraction(fraction: Float): Int {
        return (fraction * 255).roundToInt()
    }
}
