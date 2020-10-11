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

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.linkdev.android.dragdismiss.containers.ActivityContainer
import com.linkdev.android.dragdismiss.containers.FragmentContainer
import com.linkdev.android.dragdismiss.containers.IContainer
import com.linkdev.android.dragdismiss.views.DragDismissLayout
import com.linkdev.android.dragdismiss.models.DragDismissVelocityLevel
import com.linkdev.android.dragdismiss.models.DragDismissDefaults
import com.linkdev.android.dragdismiss.models.DragDismissDirections
import com.linkdev.android.dragdismiss.models.DragDismissProperties

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

    fun setDragDismissAttrs(
        dragDismissScreenPercentage: Float = DragDismissDefaults.DEFAULT_DISMISS_SCREEN_PERCENTAGE,
        dragDismissVelocityLevel: DragDismissVelocityLevel = DragDismissDefaults.DEFAULT_DISMISS_VELOCITY_LEVEL,
        draggingDirections: Int = DragDismissDefaults.DEFAULT_DRAG_DIRECTION,
        backgroundDim: Float = DragDismissDefaults.DEFAULT_BACKGROUND_DIM
    ): DragDismiss {
        mDragDismissProperties.apply {
            this.dragDismissScreenPercentage = dragDismissScreenPercentage
            this.dragDragDismissVelocityLevel = dragDismissVelocityLevel
            this.draggingDirections = draggingDirections
            this.backgroundDim = backgroundDim
        }
        return this
    }

    /**
     * The percentage of the screen traveled before It's dismissed on release.
     *
     * So if it's set to [DragDismissDefaults.DEFAULT_DISMISS_SCREEN_PERCENTAGE] then if the layout is dragged more than [DragDismissDefaults.DEFAULT_DISMISS_SCREEN_PERCENTAGE] of the screen the view will be dismissed on release.
     *
     * @default [DragDismissDefaults.DEFAULT_DISMISS_SCREEN_PERCENTAGE]
     */
    fun setDragDismissScreenPercentage(dragDismissScreenPercentage: Float): DragDismiss {
        mDragDismissProperties.dragDismissScreenPercentage = dragDismissScreenPercentage
        return this
    }

    /**
     * The speed level that if the screen is flung past it will be dismissed on release.
     *
     * @default [DragDismissDefaults.DEFAULT_DISMISS_VELOCITY_LEVEL]
     */
    fun setDragDismissVelocityLevel(dragDragDismissVelocityLevel: DragDismissVelocityLevel): DragDismiss {
        mDragDismissProperties.dragDragDismissVelocityLevel = dragDragDismissVelocityLevel
        return this
    }

    /**
     * The selected drag directions(Can be more than one direction) from [DragDismissDirections]
     *
     * @default [DragDismissDefaults.DEFAULT_DRAG_DIRECTION]
     */
    fun setDragDismissDraggingDirections(draggingDirections: Int): DragDismiss {
        mDragDismissProperties.draggingDirections = draggingDirections
        return this
    }

    /**
     * The starting alpha of the canvas background for the dismiss background fade out effect.
     *
     * @default [DragDismissDefaults.DEFAULT_BACKGROUND_DIM]
     */
    fun setDragDismissBackgroundDim(backgroundAlpha: Float): DragDismiss {
        mDragDismissProperties.backgroundDim = backgroundAlpha
        return this
    }

    /**
     * Constructs the layout using sat attrs and using defaults if not.
     * @return returns the DragDismissView
     */
    private fun constructDragDismissLayout(): DragDismissLayout {
        val dragDismissLayout = DragDismissLayout(mContext)
        dragDismissLayout.apply {
            setDragDismissDistance(mDragDismissProperties.dragDismissScreenPercentage)
            setDragDismissVelocityLevel(mDragDismissProperties.dragDragDismissVelocityLevel)
            setDraggingDirections(mDragDismissProperties.draggingDirections)
            setBackgroundDim(mDragDismissProperties.backgroundDim)
        }

        dragDismissLayout.setDismissCallback(onViewDismissed())
        return dragDismissLayout
    }

    private fun onViewDismissed(): () -> Unit = { mContainer.onDismiss() }

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
