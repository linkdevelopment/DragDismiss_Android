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

package com.linkdev.dragdismiss

import android.content.Context
import android.view.View
import androidx.annotation.IntRange
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.linkdev.dragdismiss.containers.ActivityContainer
import com.linkdev.dragdismiss.containers.FragmentContainer
import com.linkdev.dragdismiss.containers.IContainer
import com.linkdev.dragdismiss.views.DragDismissLayout
import com.linkdev.dragdismiss.models.DragDismissVelocityLevel
import com.linkdev.dragdismiss.models.DragDismissDefaults
import com.linkdev.dragdismiss.models.DragDismissDirections
import com.linkdev.dragdismiss.models.DragDismissProperties
import com.linkdev.dragdismiss.utils.Utilities

// Created on 6/2/2020.
// Copyright (c) 2020 Link Development All rights reserved.
class DragDismiss private constructor(private val mContext: Context) {

    companion object {
        fun create(context: Context): DragDismiss {
            return DragDismiss(context)
        }
    }

    private lateinit var mContainer: IContainer

    private val mDragDismissProperties = DragDismissProperties()

    /**
     * Sets all the attrs for the DragDismiss
     * @see [setDragDismissDirections], [setDragScreenPercentage], [setDragVelocityLevel], [setDragBackgroundDimPercentage]
     */
    fun setDragDismissAttrs(
        dragDismissScreenPercentage: Float = DragDismissDefaults.DEFAULT_DISMISS_SCREEN_PERCENTAGE,
        dragDismissVelocityLevel: DragDismissVelocityLevel = DragDismissDefaults.DEFAULT_DISMISS_VELOCITY_LEVEL,
        draggingDirections: DragDismissDirections = DragDismissDefaults.DEFAULT_DRAG_DIRECTION,
        backgroundDim: Float = DragDismissDefaults.DEFAULT_BACKGROUND_DIM
    ): DragDismiss {
        mDragDismissProperties.apply {
            this.dragDismissScreenPercentage =
                Utilities.percentageFromFraction(dragDismissScreenPercentage)
            this.dragDragDismissVelocityLevel = dragDismissVelocityLevel
            this.draggingDirections = draggingDirections
            this.backgroundDim = Utilities.percentageFromFraction(backgroundDim)
        }
        return this
    }

    /**
     * Sets the selected drag direction from [DragDismissDirections]
     *
     * @param draggingDirections The direction to set from [DragDismissDirections]
     * @default [DragDismissDirections.FROM_LEFT]
     */
    fun setDragDismissDirections(draggingDirections: DragDismissDirections): DragDismiss {
        mDragDismissProperties.draggingDirections = draggingDirections
        return this
    }

    /**
     * Sets the percentage of the screen traveled before the screen is dismissed on release.
     *
     * @param dragDismissScreenPercentage The percentage to set from 0 to 100.
     * @default 40
     */
    fun setDragScreenPercentage(
        @IntRange(from = 0, to = 100) dragDismissScreenPercentage: Int
    ): DragDismiss {
        mDragDismissProperties.dragDismissScreenPercentage = dragDismissScreenPercentage
        return this
    }

    /**
     * Sets the speed level that if the screen is flung past it will be dismissed on release.
     *
     * @param dragDragDismissVelocityLevel The level to set.
     * @default [DragDismissVelocityLevel.LEVEL_3]
     * @see dragDragDismissVelocityLevel
     */
    fun setDragVelocityLevel(dragDragDismissVelocityLevel: DragDismissVelocityLevel): DragDismiss {
        mDragDismissProperties.dragDragDismissVelocityLevel = dragDragDismissVelocityLevel
        return this
    }

    /**
     * Sets the starting alpha of the canvas background for the dismiss background fade out effect.
     *
     * @param backgroundDim The percentage to set from 0 to 100.
     * @default 80
     */
    fun setDragBackgroundDimPercentage(
        @IntRange(from = 0, to = 100) backgroundDim: Int
    ): DragDismiss {
        mDragDismissProperties.backgroundDim = backgroundDim
        return this
    }

    /**
     * Constructs the layout using the set attrs.
     *
     * @return returns the DragDismissView
     */
    private fun constructDragDismissLayout(): DragDismissLayout {
        val dragDismissLayout = DragDismissLayout(mContext)
        dragDismissLayout.apply {
            setDragDismissScreenPercentage(mDragDismissProperties.dragDismissScreenPercentage)
            setDragDismissVelocityLevel(mDragDismissProperties.dragDragDismissVelocityLevel)
            setDraggingDirections(mDragDismissProperties.draggingDirections)
            setBackgroundDim(mDragDismissProperties.backgroundDim)
        }

        dragDismissLayout.setDismissCallback(onViewDismissed())
        return dragDismissLayout
    }

    /**
     * Callback on the screen dismissed, To call the Container dismiss function to close the screen.
     */
    private fun onViewDismissed(): () -> Unit = { mContainer.onDismiss() }

    /**
     * Call attach to attach the drag dismiss to your layout.
     *
     * @param container The screen to be dismissed Activity or Fragment.
     * @param layoutID the layoutId for the screen to be dismissed.
     */
    fun attach(container: Any, @LayoutRes layoutID: Int): View {
        val dragDismissLayout = constructDragDismissLayout()

        mContainer = when (container) {
            is AppCompatActivity -> {
                ActivityContainer()
            }
            is Fragment -> {
                FragmentContainer()
            }
            else -> throw NotImplementedError("This container is not supported yet.")
        }

        return mContainer.attach(container, layoutID, dragDismissLayout)
    }
}
