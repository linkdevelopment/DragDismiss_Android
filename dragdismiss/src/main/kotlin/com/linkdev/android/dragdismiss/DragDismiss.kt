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
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.linkdev.android.dragdismiss.containers.ActivityContainer
import com.linkdev.android.dragdismiss.containers.IContainer
import com.linkdev.android.dragdismiss.layout.DragDismissLayout
import com.linkdev.android.dragdismiss.layout.DragDismissVelocityLevel
import com.linkdev.android.dragdismiss.utils.DragDismissDefaults
import com.linkdev.android.dragdismiss.utils.DragDismissProperties

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

    fun attach(container: Any, @LayoutRes layoutID: Int) {
        val dragDismissLayout = constructDragDismissLayout()

        when (container) {
            is AppCompatActivity -> {
                mContainer = ActivityContainer()
            }
            else -> throw NotImplementedError("This container is not supported yet.")
        }

        mContainer.attach(container, layoutID, dragDismissLayout)
    }

    fun setDragDismissAttrs(
        dragDismissScreenPercentage: Float = DragDismissDefaults.DEFAULT_DISMISS_SCREEN_PERCENTAGE,
        dragDragDismissVelocityLevel: DragDismissVelocityLevel = DragDismissDefaults.DEFAULT_DISMISS_VELOCITY_LEVEL,
        shouldDragEdgeOnly: Boolean = DragDismissDefaults.DEFAULT_SHOULD_DRAG_EDGE_ONLY,
        draggingDirections: Int = DragDismissDefaults.DEFAULT_DRAG_DIRECTION,
        backgroundAlpha: Float = DragDismissDefaults.DEFAULT_BACKGROUND_ALPHA_FRACTION
    ): DragDismiss {
        mDragDismissProperties.apply {
            this.dragDismissScreenPercentage = dragDismissScreenPercentage
            this.dragDragDismissVelocityLevel = dragDragDismissVelocityLevel
            this.shouldDragEdgeOnly = shouldDragEdgeOnly
            this.draggingDirections = draggingDirections
            this.backgroundAlpha = backgroundAlpha
        }
        return this
    }

    fun setDragDismissScreenPercentage(dragDismissScreenPercentage: Float): DragDismiss {
        mDragDismissProperties.dragDismissScreenPercentage = dragDismissScreenPercentage
        return this
    }

    fun setDragDismissVelocityLevel(dragDragDismissVelocityLevel: DragDismissVelocityLevel): DragDismiss {
        mDragDismissProperties.dragDragDismissVelocityLevel = dragDragDismissVelocityLevel
        return this
    }

    fun setShouldDragEdgeOnly(shouldDragEdgeOnly: Boolean): DragDismiss {
        mDragDismissProperties.shouldDragEdgeOnly = shouldDragEdgeOnly
        return this
    }

    fun setDragDismissDraggingDirections(draggingDirections: Int): DragDismiss {
        mDragDismissProperties.draggingDirections = draggingDirections
        return this
    }

    fun setDragDismissBackgroundAlpha(backgroundAlpha: Float): DragDismiss {
        mDragDismissProperties.backgroundAlpha = backgroundAlpha
        return this
    }

    private fun constructDragDismissLayout(): DragDismissLayout {
        val dragDismissLayout = DragDismissLayout(mContext)
        dragDismissLayout.apply {
            setDragDismissDistance(mDragDismissProperties.dragDismissScreenPercentage)
            setDragDismissVelocityLevel(mDragDismissProperties.dragDragDismissVelocityLevel)
            shouldDragEdgeOnly(mDragDismissProperties.shouldDragEdgeOnly)
            setDragDirections(mDragDismissProperties.draggingDirections)
            setBackgroundAlpha(mDragDismissProperties.backgroundAlpha)
        }

        dragDismissLayout.setDismissCallback(onViewDismissed())
        return dragDismissLayout
    }

    private fun onViewDismissed(): () -> Unit = { mContainer.onDismiss() }
}
