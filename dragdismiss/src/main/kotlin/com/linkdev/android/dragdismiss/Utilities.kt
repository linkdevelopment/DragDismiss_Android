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
