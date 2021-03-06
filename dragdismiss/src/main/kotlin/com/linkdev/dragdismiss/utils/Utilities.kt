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

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import com.linkdev.dragdismiss.models.DragDismissDirections
import kotlin.math.roundToInt

/**
 * Created on 5/22/2018.
 */
internal object Utilities {

    internal fun findAllScrollViews(viewGroup: ViewGroup): ArrayList<View> {
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

    internal fun fractionFromPercentage(percentage: Int): Float {
        return percentage / 100f
    }

    internal fun percentageFromFraction(fraction: Float): Int {
        return (fraction * 100).toInt()
    }

    internal fun calculateAlphaFromFraction(fraction: Float): Int {
        return (fraction * 255).roundToInt()
    }

    internal fun calculateFractionFromAlpha(alpha: Int): Float {
        return alpha / 255.0f
    }
}
